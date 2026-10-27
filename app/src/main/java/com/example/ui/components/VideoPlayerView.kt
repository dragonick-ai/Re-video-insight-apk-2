package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoAspectRatio
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoOutline
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurfaceLowest
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerView(
  aspectRatio: VideoAspectRatio = VideoAspectRatio.LANDSCAPE,
  durationSeconds: Int = 15,
  seedLabel: String = "Seed #1",
  generatedVideoUriString: String? = null,
  referenceVideoUriString: String? = null,
  onToast: (String, String) -> Unit = { _, _ -> },
  modifier: Modifier = Modifier
) {
  var isPlaying by remember { mutableStateOf(false) }
  var isLooping by remember { mutableStateOf(true) }
  var isCompareMode by remember { mutableStateOf(false) } // False = AI Generated Output, True = Original Reference
  var currentProgress by remember { mutableFloatStateOf(0f) }
  var actualDurationSec by remember { mutableStateOf(durationSeconds.coerceAtLeast(1)) }

  val activeVideoUri = if (isCompareMode) referenceVideoUriString else generatedVideoUriString

  // Progress updater when playing
  LaunchedEffect(isPlaying, isLooping, activeVideoUri) {
    if (activeVideoUri.isNullOrBlank()) {
      isPlaying = false
      return@LaunchedEffect
    }
    while (isPlaying) {
      delay(100)
      currentProgress += (0.1f / actualDurationSec.toFloat())
      if (currentProgress >= 1f) {
        if (isLooping) {
          currentProgress = 0f
        } else {
          currentProgress = 1f
          isPlaying = false
        }
      }
    }
  }

  val currentSeconds = (currentProgress * actualDurationSec).toInt()
  val timeFormatted = String.format("%d:%02d", currentSeconds / 60, currentSeconds % 60)
  val totalFormatted = String.format("%d:%02d", actualDurationSec / 60, actualDurationSec % 60)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(ReVideoSurfaceLowest)
      .border(1.dp, ReVideoBorder, RoundedCornerShape(16.dp))
  ) {
    // Viewport box
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(aspectRatio.ratioFloat)
        .background(Color.Black)
        .testTag("video_player_stage")
    ) {
      if (!activeVideoUri.isNullOrBlank()) {
        AndroidView(
          factory = { ctx ->
            VideoView(ctx).apply {
              try {
                setVideoURI(Uri.parse(activeVideoUri))
                setOnPreparedListener { mp ->
                  mp.isLooping = isLooping
                  val durSec = (mp.duration / 1000).coerceAtLeast(1)
                  actualDurationSec = durSec
                  if (isPlaying) start()
                }
              } catch (_: Exception) {}
            }
          },
          update = { vView ->
            try {
              if (vView.tag != activeVideoUri) {
                vView.tag = activeVideoUri
                vView.setVideoURI(Uri.parse(activeVideoUri))
              }
              if (isPlaying && !vView.isPlaying) {
                vView.start()
              } else if (!isPlaying && vView.isPlaying) {
                vView.pause()
              }
            } catch (_: Exception) {}
          },
          modifier = Modifier.fillMaxSize().testTag("native_video_view")
        )
      } else {
        // Honest diagnostic state when no video stream is available (never fake a Canvas animation)
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.VideocamOff,
            contentDescription = null,
            tint = ReVideoOutline,
            modifier = Modifier.size(32.dp)
          )
          Text(
            text = if (isCompareMode) {
              "No Reference Video File Attached"
            } else {
              "No Generated MP4 Stream Available"
            },
            color = ReVideoTextHigh,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
          )
          Text(
            text = if (isCompareMode) {
              "Select a reference video from your device in Create Studio to preview the original source clip."
            } else {
              "Submit a video-to-video request with an active Gemini Omni 1.1 Flash endpoint, or tap Compare to inspect your uploaded reference clip."
            },
            color = ReVideoTextMedium,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }

      // Top-left live mode badge
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(10.dp)
          .clip(RoundedCornerShape(9999.dp))
          .background(Color.Black.copy(alpha = 0.65f))
          .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(9999.dp))
          .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
      ) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(
              when {
                activeVideoUri.isNullOrBlank() -> ReVideoOutline
                isCompareMode -> Color(0xFFFFB266)
                else -> ReVideoEmerald
              }
            )
        )
        Text(
          text = if (isCompareMode) "Original Reference Clip" else "Generated Video ($seedLabel)",
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }

      // Center play/pause floating button
      if (!activeVideoUri.isNullOrBlank()) {
        IconButton(
          onClick = {
            isPlaying = !isPlaying
            onToast(
              if (isPlaying) "Playing video stream" else "Playback paused",
              if (isPlaying) "play_circle" else "pause"
            )
          },
          modifier = Modifier
            .align(Alignment.Center)
            .size(54.dp)
            .clip(CircleShape)
            .background(ReVideoPrimary)
            .testTag("playback_toggle_button")
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
          )
        }
      }
    }

    // Scrubber and controls bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(ReVideoSurfaceLowest)
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      // Interactive Scrubber Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(18.dp)
          .pointerInput(Unit) {
            detectDragGestures(
              onDrag = { change, _ ->
                val newProgress = (change.position.x / size.width.toFloat()).coerceIn(0f, 1f)
                currentProgress = newProgress
              }
            )
          }
          .clickable {},
        contentAlignment = Alignment.CenterStart
      ) {
        // Track
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(ReVideoSurface2)
        ) {
          // Filled progress
          Box(
            modifier = Modifier
              .fillMaxWidth(currentProgress)
              .height(4.dp)
              .background(ReVideoPrimary)
          )
        }
      }

      // Metadata & buttons row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Time & aspect ratio badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = timeFormatted,
            color = ReVideoTextHigh,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "/",
            color = ReVideoOutline,
            fontSize = 12.sp
          )
          Text(
            text = totalFormatted,
            color = ReVideoTextMedium,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(ReVideoSurface2)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = aspectRatio.label,
              color = ReVideoPrimary,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Action controls
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Loop toggle
          IconButton(
            onClick = {
              isLooping = !isLooping
              onToast(if (isLooping) "Loop enabled" else "Loop disabled", "repeat")
            },
            modifier = Modifier
              .size(30.dp)
              .testTag("loop_button")
          ) {
            Icon(
              imageVector = Icons.Default.Repeat,
              contentDescription = "Loop",
              tint = if (isLooping) ReVideoPrimary else ReVideoTextMedium,
              modifier = Modifier.size(17.dp)
            )
          }

          // Compare button
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(if (isCompareMode) ReVideoPrimary else ReVideoSurface2)
              .clickable {
                isCompareMode = !isCompareMode
                isPlaying = false
                currentProgress = 0f
                onToast(
                  if (isCompareMode) "Showing Original Reference Video" else "Showing Generated Video Output",
                  "compare"
                )
              }
              .padding(horizontal = 10.dp, vertical = 5.dp)
              .testTag("compare_button"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Compare,
              contentDescription = "Compare",
              tint = if (isCompareMode) Color.White else ReVideoCyan,
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = "Compare",
              color = if (isCompareMode) Color.White else ReVideoTextHigh,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }

          // Fullscreen toggle
          IconButton(
            onClick = {
              onToast("Fullscreen aspect ratio (${aspectRatio.label})", "fullscreen")
            },
            modifier = Modifier
              .size(30.dp)
              .testTag("fullscreen_button")
          ) {
            Icon(
              imageVector = Icons.Default.Fullscreen,
              contentDescription = "Fullscreen",
              tint = ReVideoTextMedium,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
