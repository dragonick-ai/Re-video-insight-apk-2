package com.example.data.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import com.example.data.model.KeyframeSample
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

data class ExtractedVideoInfo(
  val displayName: String,
  val mimeType: String,
  val sizeBytes: Long,
  val durationMs: Long,
  val durationFormatted: String,
  val width: Int,
  val height: Int,
  val frameRate: String,
  val thumbnail: Bitmap?,
  val videoBase64: String?,
  val keyframeBase64List: List<String>,
  val timestampedKeyframes: List<KeyframeSample>
)

object VideoMetadataHelper {

  private const val MAX_INLINE_VIDEO_BYTES = 15L * 1024L * 1024L // 15 MB inline limit for Gemini API

  fun formatTimestamp(ms: Long): String {
    val totalSec = (ms / 1000L).coerceAtLeast(0L).toInt()
    val mins = totalSec / 60
    val secs = totalSec % 60
    return String.format("%d:%02d", mins, secs)
  }

  fun extractVideoInfo(context: Context, uri: Uri): ExtractedVideoInfo {
    var displayName = "Selected_Video.mp4"
    var sizeBytes = 0L
    val detectedMime = try {
      context.contentResolver.getType(uri)
    } catch (_: Exception) {
      null
    } ?: "video/mp4"

    try {
      context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (cursor.moveToFirst()) {
          if (nameIndex != -1) displayName = cursor.getString(nameIndex) ?: displayName
          if (sizeIndex != -1) sizeBytes = cursor.getLong(sizeIndex)
        }
      }
    } catch (_: Exception) {
      displayName = uri.lastPathSegment ?: "Selected_Video.mp4"
    }

    // Read raw video bytes if within inline limit so we can pass the actual reference video to Gemini
    var videoBase64: String? = null
    try {
      if (sizeBytes in 1..MAX_INLINE_VIDEO_BYTES) {
        context.contentResolver.openInputStream(uri)?.use { input ->
          val bytes = input.readBytes()
          if (bytes.isNotEmpty() && bytes.size <= MAX_INLINE_VIDEO_BYTES) {
            sizeBytes = bytes.size.toLong()
            videoBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
          }
        }
      } else if (sizeBytes == 0L) {
        // Stream size wasn't reported by cursor; read up to MAX_INLINE_VIDEO_BYTES
        context.contentResolver.openInputStream(uri)?.use { input ->
          val buffer = ByteArrayOutputStream()
          val chunk = ByteArray(16 * 1024)
          var totalRead = 0L
          var read: Int
          while (input.read(chunk).also { read = it } != -1) {
            totalRead += read
            if (totalRead <= MAX_INLINE_VIDEO_BYTES) {
              buffer.write(chunk, 0, read)
            } else {
              break
            }
          }
          sizeBytes = totalRead
          if (totalRead in 1..MAX_INLINE_VIDEO_BYTES) {
            videoBase64 = Base64.encodeToString(buffer.toByteArray(), Base64.NO_WRAP)
          }
        }
      }
    } catch (_: Exception) {
      // Fallback to keyframe extraction if raw stream reading fails
    }

    val retriever = MediaMetadataRetriever()
    var durationMs = 0L
    var width = 1920
    var height = 1080
    var fpsString = "30fps"
    var thumbnail: Bitmap? = null
    val keyframes = mutableListOf<String>()
    val timestampedKeyframes = mutableListOf<KeyframeSample>()

    try {
      retriever.setDataSource(context, uri)
      durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 15000L
      width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 1920
      height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 1080
      val captureFps = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)?.toFloatOrNull()
      if (captureFps != null && captureFps > 0f) {
        fpsString = "${captureFps.toInt()}fps"
      }

      // Extract initial thumbnail frame
      val timeUs = (durationMs.coerceAtLeast(1000L) / 4) * 1000L
      thumbnail = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

      // Sample up to 5 timestamped keyframes across the clip for multimodal Gemini video analysis
      val samplePoints = listOf(0.10, 0.30, 0.50, 0.70, 0.90)
      for (point in samplePoints) {
        val sampleMs = (durationMs * point).toLong()
        val sampleTimeUs = sampleMs * 1000L
        val frame = retriever.getFrameAtTime(sampleTimeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        if (frame != null) {
          val targetWidth = 512
          val targetHeight = (targetWidth * (frame.height.toFloat() / frame.width.coerceAtLeast(1))).toInt().coerceAtLeast(1)
          val scaled = Bitmap.createScaledBitmap(frame, targetWidth, targetHeight, true)
          val stream = ByteArrayOutputStream()
          scaled.compress(Bitmap.CompressFormat.JPEG, 78, stream)
          val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
          keyframes.add(base64)
          timestampedKeyframes.add(
            KeyframeSample(
              timestamp = formatTimestamp(sampleMs),
              base64 = base64
            )
          )
        }
      }
    } catch (_: Exception) {
      // Fallback if retriever fails
    } finally {
      try {
        retriever.release()
      } catch (_: Exception) {}
    }

    val formattedDuration = formatTimestamp(durationMs)

    return ExtractedVideoInfo(
      displayName = displayName,
      mimeType = detectedMime,
      sizeBytes = sizeBytes,
      durationMs = durationMs,
      durationFormatted = formattedDuration,
      width = width,
      height = height,
      frameRate = fpsString,
      thumbnail = thumbnail,
      videoBase64 = videoBase64,
      keyframeBase64List = keyframes,
      timestampedKeyframes = timestampedKeyframes
    )
  }

  fun saveGeneratedVideoToDownloads(
    context: Context,
    videoUriString: String,
    outputFileName: String
  ): Result<String> {
    return try {
      val uri = Uri.parse(videoUriString)
      val inputStream = when (uri.scheme) {
        "file" -> FileInputStream(File(uri.path ?: return Result.failure(IllegalArgumentException("Invalid file path"))))
        "content" -> context.contentResolver.openInputStream(uri)
        else -> {
          val directFile = File(videoUriString)
          if (directFile.exists()) FileInputStream(directFile) else null
        }
      } ?: return Result.failure(IllegalStateException("Unable to open generated video stream."))

      inputStream.use { input ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, outputFileName)
            put(MediaStore.Downloads.MIME_TYPE, "video/mp4")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
          }
          val resolver = context.contentResolver
          val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
          val itemUri = resolver.insert(collection, values)
          if (itemUri != null) {
            resolver.openOutputStream(itemUri)?.use { output ->
              input.copyTo(output)
            }
            return Result.success("Downloads/$outputFileName")
          }
        }

        // Fallback to app external files directory
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
        val outFile = File(downloadsDir, outputFileName)
        FileOutputStream(outFile).use { output ->
          input.copyTo(output)
        }
        Result.success(outFile.absolutePath)
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
