package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VideoPickerDialog
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoError
import com.example.ui.theme.ReVideoOutline
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurfaceLowest
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.ui.theme.ReVideoTextSubtle
import com.example.viewmodel.AppTab
import com.example.viewmodel.CreateStep
import com.example.viewmodel.ReVideoViewModel

@Composable
fun CreateUploadScreen(
  viewModel: ReVideoViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val selectedClip by viewModel.selectedClip.collectAsState()
  val isAnalyzing by viewModel.isAnalyzing.collectAsState()
  val analysisProgress by viewModel.analysisProgress.collectAsState()
  val analysisStatusText by viewModel.analysisStatusText.collectAsState()
  val analysisResult by viewModel.analysisResult.collectAsState()
  val editableBasePrompt by viewModel.editableBasePrompt.collectAsState()
  val analysisError by viewModel.analysisErrorMessage.collectAsState()

  var showClipPickerModal by remember { mutableStateOf(false) }

  // 1. Native Device File Picker (GetContent for all video formats)
  val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    if (uri != null) {
      viewModel.selectClipFromUri(context, uri)
    }
  }

  // 2. Modern Android Photo/Video Picker launcher
  val mediaPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      viewModel.selectClipFromUri(context, uri)
    }
  }

  if (showClipPickerModal) {
    VideoPickerDialog(
      sampleClips = viewModel.sampleClips,
      selectedClip = selectedClip,
      onSelectClip = { clip -> viewModel.selectClip(clip) },
      onLaunchDevicePicker = {
        filePickerLauncher.launch("video/*")
      },
      onDismiss = { showClipPickerModal = false }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Stepper Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(14.dp)
        .testTag("create_stepper_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Step 1
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(ReVideoPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Text(
              text = "Upload & Analyze",
              color = ReVideoPrimaryLight,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ReVideoOutline,
            modifier = Modifier.size(14.dp)
          )

          // Step 2
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.clickable {
              if (selectedClip != null) {
                viewModel.setCreateStep(CreateStep.COMBINE_IDEA)
              }
            }
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(ReVideoSurface2),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "2", color = ReVideoTextMedium, fontSize = 11.sp)
            }
            Text(text = "Combine", color = ReVideoTextMedium, fontSize = 13.sp)
          }

          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ReVideoOutline,
            modifier = Modifier.size(14.dp)
          )

          // Step 3
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(ReVideoSurface2),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "3", color = ReVideoTextMedium, fontSize = 11.sp)
            }
            Text(text = "Generate", color = ReVideoTextMedium, fontSize = 13.sp)
          }
        }

        // Stepper Progress bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(ReVideoSurface2)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(if (analysisResult != null) 0.33f else 0.12f)
              .height(4.dp)
              .background(ReVideoPrimary)
          )
        }
      }
    }

    // 2. Source Video Clip Card
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = null,
            tint = ReVideoCyan,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Source Clip",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(ReVideoSurface2)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (selectedClip != null) ReVideoEmerald else ReVideoOutline)
            )
            Text(
              text = if (selectedClip != null) "Attached (${selectedClip?.duration})" else "Awaiting Media",
              color = ReVideoTextMedium,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      val clip = selectedClip
      if (clip == null) {
        // Dashed select box (when no video is selected yet)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorder, RoundedCornerShape(12.dp))
            .padding(22.dp)
            .testTag("source_clip_select_container"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(ReVideoSurface2)
                .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(14.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.VideoFile,
                contentDescription = null,
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(26.dp)
              )
            }

            Text(
              text = "Select reference video from device",
              color = ReVideoTextHigh,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )

            Text(
              text = "MP4 or MOV, up to 60s, max 15MB inline",
              color = ReVideoTextMedium,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )

            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.padding(top = 4.dp)
            ) {
              // Primary native file picker button
              Button(
                onClick = { filePickerLauncher.launch("video/*") },
                colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp).testTag("native_video_picker_btn")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                  Text("Browse Device Files", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
              }

              // Photo Picker / Sample Clip Options dialog button
              Button(
                onClick = {
                  mediaPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                },
                colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
                border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp).testTag("browse_media_btn")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(Icons.Default.Upload, contentDescription = null, tint = ReVideoCyan, modifier = Modifier.size(15.dp))
                  Text("Media Picker", color = ReVideoTextHigh, fontSize = 12.sp)
                }
              }
            }
          }
        }
      } else {
        // Populated attached video card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(14.dp)
            .testTag("attached_clip_card")
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ReVideoSurface2),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = ReVideoCyan,
                    modifier = Modifier.size(24.dp)
                  )
                }

                Column {
                  Text(
                    text = clip.name,
                    color = ReVideoTextHigh,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                  val payloadBadge = when {
                    !clip.videoBase64.isNullOrBlank() -> "Inline Video + ${clip.timestampedKeyframes.size} Frames"
                    clip.keyframeBase64List.isNotEmpty() -> "${clip.keyframeBase64List.size} Extracted Frames"
                    else -> "Metadata Attached"
                  }
                  Text(
                    text = "${clip.duration} • ${clip.resolution} • $payloadBadge",
                    color = ReVideoTextMedium,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                }
              }

              IconButton(
                onClick = { viewModel.clearSelectedClip() },
                modifier = Modifier.size(28.dp)
              ) {
                Icon(Icons.Default.Close, contentDescription = "Remove clip", tint = ReVideoTextMedium, modifier = Modifier.size(16.dp))
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { filePickerLauncher.launch("video/*") },
                colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp)
              ) {
                Text("Change Video", color = ReVideoTextHigh, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // 3. Clear Error or Setup Card if service is unavailable
    if (analysisError != null) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF2B1619))
          .border(1.dp, ReVideoError.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
          .padding(14.dp)
          .testTag("analysis_error_banner")
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ReVideoError, modifier = Modifier.size(18.dp))
            Text(
              text = "Analysis Service Message",
              color = ReVideoError,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = analysisError ?: "",
            color = ReVideoTextHigh,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = { viewModel.selectTab(AppTab.SETTINGS) },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.height(32.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Key, contentDescription = null, tint = ReVideoCyan, modifier = Modifier.size(14.dp))
                Text("Open Settings", color = ReVideoTextHigh, fontSize = 11.sp)
              }
            }

            Button(
              onClick = { viewModel.startVideoAnalysis() },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.height(32.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Text("Retry Analysis", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // 4. Inference Model Engine Status (Gemini 4 Argon)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(14.dp)
        .testTag("analysis_pipeline_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column {
            Text(
              text = "ANALYSIS PIPELINE",
              color = ReVideoCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = "Gemini 4 Argon Vision",
              color = ReVideoTextHigh,
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(top = 2.dp)
            )
            Text(
              text = "Extracts scene, actions, camera motion, visual style, lighting & timestamps",
              color = ReVideoTextMedium,
              fontSize = 11.sp,
              lineHeight = 15.sp
            )
          }

          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(ReVideoSurface2)
              .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Progress bar container
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ReVideoSurfaceLowest)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp))
            .padding(10.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.HourglassEmpty,
                  contentDescription = null,
                  tint = ReVideoOutline,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = analysisStatusText,
                  color = ReVideoTextMedium,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }
              Text(
                text = "$analysisProgress%",
                color = ReVideoPrimaryLight,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(ReVideoSurface2)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth((analysisProgress / 100f).coerceIn(0f, 1f))
                  .height(4.dp)
                  .background(ReVideoCyan)
              )
            }

            Text(
              text = if (selectedClip == null) "Upload a video to deconstruct scene descriptions, actions, camera movement, lighting, and timestamps."
              else "Ready: Gemini 4 Argon will analyze the reference video and generate an editable base prompt.",
              color = ReVideoTextSubtle,
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // 5. Scene Decomposition Layer Cards (Scene, Actions, Camera, Style, Lighting, Timestamps)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Layers,
            contentDescription = null,
            tint = ReVideoPrimaryLight,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Scene Decomposition",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(ReVideoSurface2)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "${analysisResult?.layers?.size ?: 0} Layers",
            color = ReVideoTextMedium,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      val result = analysisResult
      if (result == null || result.layers.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
            .padding(20.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ViewInAr,
              contentDescription = null,
              tint = ReVideoOutline,
              modifier = Modifier.size(22.dp)
            )
            Text(
              text = "Scene breakdown (scene description, actions, camera motion, visual style, lighting, and timestamps) will appear here once analyzed.",
              color = ReVideoTextMedium,
              fontSize = 12.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              lineHeight = 16.sp
            )
          }
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          result.layers.forEach { layer ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ReVideoSurface1)
                .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(10.dp))
                .padding(12.dp)
                .testTag("scene_layer_${layer.id}")
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = layer.title,
                    color = ReVideoTextHigh,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                  Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    if (!layer.timestampRange.isNullOrBlank()) {
                      Text(
                        text = layer.timestampRange,
                        color = ReVideoPrimaryLight,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                      )
                    }
                    Text(
                      text = layer.type,
                      color = ReVideoCyan,
                      fontSize = 10.sp,
                      fontFamily = FontFamily.Monospace
                    )
                  }
                }
                Text(
                  text = layer.description,
                  color = ReVideoTextMedium,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }
      }
    }

    // 6. Editable Extracted Base Prompt
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AutoFixHigh,
            contentDescription = null,
            tint = ReVideoPrimaryLight,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Extracted Base Prompt",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (analysisResult != null) {
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ReVideoSurface2)
                .clickable { viewModel.resetBasePromptToAnalyzed() }
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .testTag("reset_base_prompt_btn"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, tint = ReVideoCyan, modifier = Modifier.size(12.dp))
              Text("Reset", color = ReVideoCyan, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
          }

          Text(
            text = if (analysisResult != null) "Editable" else "Idle",
            color = if (analysisResult != null) ReVideoEmerald else ReVideoOutline,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ReVideoSurface1)
          .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        if (analysisResult == null && editableBasePrompt.isBlank()) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              tint = ReVideoOutline,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Editable prompt blueprint will be automatically generated from video analysis results.",
              color = ReVideoTextMedium,
              fontSize = 12.sp
            )
          }
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Edit, contentDescription = null, tint = ReVideoPrimaryLight, modifier = Modifier.size(13.dp))
              Text(
                text = "Edit analyzed base prompt before combining with your creative idea:",
                color = ReVideoTextSubtle,
                fontSize = 11.sp
              )
            }
            TextField(
              value = editableBasePrompt,
              onValueChange = { viewModel.updateEditableBasePrompt(it) },
              colors = TextFieldDefaults.colors(
                focusedContainerColor = ReVideoSurfaceLowest,
                unfocusedContainerColor = ReVideoSurfaceLowest,
                disabledContainerColor = ReVideoSurfaceLowest,
                focusedIndicatorColor = ReVideoPrimary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = ReVideoTextHigh,
                unfocusedTextColor = ReVideoTextHigh
              ),
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .testTag("editable_base_prompt_input")
            )
          }
        }
      }
    }

    // Sticky Bottom Action Area
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = {
            viewModel.selectTab(AppTab.HOME)
          },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
          border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(44.dp)
            .testTag("upload_back_button")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              tint = ReVideoTextHigh,
              modifier = Modifier.size(16.dp)
            )
            Text("Back", color = ReVideoTextHigh, fontSize = 13.sp)
          }
        }

        Button(
          onClick = {
            if (analysisResult == null) {
              viewModel.startVideoAnalysis()
            } else {
              viewModel.proceedToCombineIdea()
            }
          },
          enabled = selectedClip != null && !isAnalyzing,
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("analyze_reference_video_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = if (isAnalyzing) "Analyzing with Gemini 4 Argon..."
              else if (analysisResult != null) "Proceed to Combine Idea"
              else "Analyze Reference Video",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            Icon(
              imageVector = if (analysisResult != null) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Psychology,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      Text(
        text = if (selectedClip == null) "Select a video to begin analysis" else "Reference clip attached • Gemini 4 Argon ready",
        color = ReVideoTextSubtle,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace
      )
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}
