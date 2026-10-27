package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectStatus
import com.example.data.util.VideoMetadataHelper
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoError
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
fun GenerationResultScreen(
  viewModel: ReVideoViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val activeProject by viewModel.activeProject.collectAsState()

  var isAccordionOpen by remember { mutableStateOf(true) }
  var showMoreMenu by remember { mutableStateOf(false) }

  val project = activeProject
  if (project == null) {
    // Empty state when user hasn't generated a video yet
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Videocam,
            contentDescription = null,
            tint = ReVideoPrimaryLight,
            modifier = Modifier.size(32.dp)
          )
        }

        Text(
          text = "No Generation Active",
          color = ReVideoTextHigh,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = "Upload a reference video and synthesize your prompt in Create Studio to generate your first video-to-video render.",
          color = ReVideoTextMedium,
          fontSize = 13.sp,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
          onClick = {
            viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
          },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.height(44.dp)
        ) {
          Text("Start Video Workflow", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }
    return
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Screen Sub-Header & Controls
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.weight(1f, fill = false)
      ) {
        IconButton(
          onClick = {
            viewModel.setCreateStep(CreateStep.COMBINE_IDEA)
          },
          modifier = Modifier.size(36.dp).testTag("result_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = ReVideoTextHigh,
            modifier = Modifier.size(20.dp)
          )
        }

        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = project.title,
              color = ReVideoTextHigh,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            if (project.status == ProjectStatus.COMPLETED && !project.videoOutputUri.isNullOrBlank()) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified",
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(15.dp)
              )
            }
          }

          Text(
            text = "${project.subtitle} • ${project.timestamp}",
            color = ReVideoTextMedium,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Share action
        IconButton(
          onClick = {
            val shareIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(
                Intent.EXTRA_TEXT,
                "Check out my neural video generation from ReVideo AI!\n\nPrompt: ${project.synthesizedPrompt}\nModel: ${project.generationModel}"
              )
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
            viewModel.showToast("Opening sharing sheet...", "share")
          },
          modifier = Modifier.size(34.dp).testTag("top_share_button")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = ReVideoTextMedium,
            modifier = Modifier.size(18.dp)
          )
        }

        // More Options
        Box {
          IconButton(
            onClick = { showMoreMenu = !showMoreMenu },
            modifier = Modifier.size(34.dp).testTag("more_options_button")
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "More",
              tint = ReVideoTextMedium,
              modifier = Modifier.size(18.dp)
            )
          }

          DropdownMenu(
            expanded = showMoreMenu,
            onDismissRequest = { showMoreMenu = false }
          ) {
            DropdownMenuItem(
              text = { Text("Edit Pipeline") },
              onClick = {
                showMoreMenu = false
                viewModel.setCreateStep(CreateStep.COMBINE_IDEA)
              }
            )
            DropdownMenuItem(
              text = { Text("Re-seed Permutation") },
              onClick = {
                showMoreMenu = false
                viewModel.showToast("Re-rolled alternative seed permutations", "casino")
              }
            )
            DropdownMenuItem(
              text = { Text("Delete Project") },
              onClick = {
                showMoreMenu = false
                viewModel.deleteProject(project.id)
                viewModel.selectTab(AppTab.HOME)
              }
            )
          }
        }
      }
    }

    // If project failed, show real failure banner
    if (project.status == ProjectStatus.FAILED) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF2B1619))
          .border(1.dp, ReVideoError.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
          .padding(14.dp)
          .testTag("generation_failure_banner")
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ReVideoError, modifier = Modifier.size(18.dp))
            Text("Generation Service Notice", color = ReVideoError, fontSize = 14.sp, fontWeight = FontWeight.Bold)
          }
          Text(
            text = project.errorMessage ?: "Video-to-Video generation failed. Please check your network and API key configuration.",
            color = ReVideoTextHigh,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = { viewModel.setCreateStep(CreateStep.COMBINE_IDEA) },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.height(34.dp)
            ) {
              Text("Back to Editor", color = ReVideoTextHigh, fontSize = 12.sp)
            }
            Button(
              onClick = { viewModel.selectTab(AppTab.SETTINGS) },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.height(34.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(14.dp))
                Text("API Settings", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }

    // 2. Main Video Player Stage (plays real generated video output or original reference video in Compare mode)
    VideoPlayerView(
      aspectRatio = project.aspectRatio,
      durationSeconds = (project.referenceClip.durationMs / 1000L).toInt().coerceAtLeast(1),
      seedLabel = project.seeds.getOrNull(project.activeSeedIndex)?.label ?: "Seed #1",
      generatedVideoUriString = project.videoOutputUri,
      referenceVideoUriString = project.referenceClip.uriString,
      onToast = { msg, icon -> viewModel.showToast(msg, icon) }
    )

    // 3. Primary Action Toolbar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(14.dp)
        .testTag("action_toolbar_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // High Contrast Primary Action: Save real generated MP4 file
        Button(
          onClick = {
            val outUri = project.videoOutputUri
            if (outUri.isNullOrBlank()) {
              viewModel.showToast(
                "No generated MP4 video available to download. Complete a valid video generation first.",
                "warning"
              )
            } else {
              val saveResult = VideoMetadataHelper.saveGeneratedVideoToDownloads(
                context = context,
                videoUriString = outUri,
                outputFileName = "ReVideo_${project.id}.mp4"
              )
              saveResult.fold(
                onSuccess = { savedPath ->
                  viewModel.showToast("Saved MP4 to $savedPath", "download")
                },
                onFailure = { err ->
                  viewModel.showToast("Export failed: ${err.message}", "error")
                }
              )
            }
          },
          enabled = !project.videoOutputUri.isNullOrBlank(),
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
          shape = RoundedCornerShape(9999.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("download_mp4_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = if (!project.videoOutputUri.isNullOrBlank()) "Download MP4 Video" else "MP4 Unavailable",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            if (project.fileSizeMb > 0.0) {
              Text(
                text = "${project.fileSizeMb} MB",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        // 3-Way Control Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Remix
          Button(
            onClick = {
              viewModel.setCreateStep(CreateStep.COMBINE_IDEA)
              viewModel.showToast("Duplicating workflow into editor...", "autorenew")
            },
            colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
            border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
            shape = RoundedCornerShape(9999.dp),
            modifier = Modifier.weight(1f).height(38.dp).testTag("remix_btn")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Autorenew, contentDescription = null, tint = ReVideoCyan, modifier = Modifier.size(15.dp))
              Text("Remix", color = ReVideoTextHigh, fontSize = 11.sp)
            }
          }

          // Share
          Button(
            onClick = {
              val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                  Intent.EXTRA_TEXT,
                  "ReVideo AI Video-to-Video: ${project.title}\n${project.synthesizedPrompt}"
                )
                type = "text/plain"
              }
              context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
              viewModel.showToast("Exporting to socials...", "send")
            },
            colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
            border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
            shape = RoundedCornerShape(9999.dp),
            modifier = Modifier.weight(1f).height(38.dp).testTag("share_btn")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = ReVideoPrimaryLight, modifier = Modifier.size(15.dp))
              Text("Share", color = ReVideoTextHigh, fontSize = 11.sp)
            }
          }

          // Save Library
          Button(
            onClick = {
              viewModel.toggleFavorite(project.id)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
            border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
            shape = RoundedCornerShape(9999.dp),
            modifier = Modifier.weight(1f).height(38.dp).testTag("save_library_btn")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = if (project.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = null,
                tint = ReVideoEmerald,
                modifier = Modifier.size(15.dp)
              )
              Text("Save Library", color = ReVideoTextHigh, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // 4. Generation Parameters & Metadata Accordion
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .testTag("metadata_accordion")
    ) {
      Column {
        // Accordion Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { isAccordionOpen = !isAccordionOpen }
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Prompt & Neural Weights",
              color = ReVideoTextHigh,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Icon(
            imageVector = if (isAccordionOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = null,
            tint = ReVideoTextMedium
          )
        }

        AnimatedVisibility(visible = isAccordionOpen) {
          Column(
            modifier = Modifier.padding(start = 14.dp, end = 14.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Prompt Box
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ReVideoSurfaceLowest)
                .padding(12.dp)
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "SYNTHESIZED PROMPT (${project.generationModeLabel.uppercase()})",
                    color = ReVideoTextSubtle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                  )

                  Row(
                    modifier = Modifier.clickable {
                      clipboardManager.setText(AnnotatedString(project.synthesizedPrompt))
                      viewModel.showToast("Full prompt copied to clipboard!", "assignment_turned_in")
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.ContentCopy,
                      contentDescription = "Copy",
                      tint = ReVideoCyan,
                      modifier = Modifier.size(13.dp)
                    )
                    Text(
                      text = "Copy",
                      color = ReVideoCyan,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }

                Text(
                  text = project.synthesizedPrompt,
                  color = ReVideoTextHigh,
                  fontSize = 12.sp,
                  lineHeight = 17.sp
                )
              }
            }

            // Video Source Reference & Style Weights Grid
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Source Reference Card
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(ReVideoSurfaceLowest)
                  .padding(10.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(ReVideoSurface2),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.VideoFile,
                      contentDescription = null,
                      tint = ReVideoCyan,
                      modifier = Modifier.size(18.dp)
                    )
                  }

                  Column {
                    Text(text = "Input Source", color = ReVideoTextSubtle, fontSize = 10.sp)
                    Text(
                      text = project.referenceClip.name.take(14),
                      color = ReVideoTextHigh,
                      fontSize = 12.sp,
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.Medium
                    )
                    Text(
                      text = if (project.referenceClip.isSampleClip) "Studio Sample" else "Device Video",
                      color = ReVideoCyan,
                      fontSize = 10.sp
                    )
                  }
                }
              }

              // Sliders Visualization
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(ReVideoSurfaceLowest)
                  .padding(10.dp)
              ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                  // Motion
                  Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                      Text(text = "Motion", color = ReVideoTextSubtle, fontSize = 10.sp)
                      Text(
                        text = "${project.diffusionParameters.motionFidelity}%",
                        color = ReVideoPrimaryLight,
                        fontSize = 10.sp,
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
                          .fillMaxWidth((project.diffusionParameters.motionFidelity / 100f).coerceIn(0f, 1f))
                          .height(4.dp)
                          .background(ReVideoPrimary)
                      )
                    }
                  }

                  // Style Transfer
                  Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                      Text(text = "Style Transfer", color = ReVideoTextSubtle, fontSize = 10.sp)
                      Text(
                        text = "${project.diffusionParameters.styleOverwrite}%",
                        color = ReVideoCyan,
                        fontSize = 10.sp,
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
                          .fillMaxWidth((project.diffusionParameters.styleOverwrite / 100f).coerceIn(0f, 1f))
                          .height(4.dp)
                          .background(ReVideoCyan)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    // 5. Alternative Seeds Carousel
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
          Text(
            text = "Alternative Seeds",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(ReVideoSurface2)
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "${project.seeds.size} Seeds",
              color = ReVideoCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Row(
          modifier = Modifier.clickable {
            viewModel.showToast("Re-rolling alternative seed permutations...", "casino")
          },
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Casino,
            contentDescription = null,
            tint = ReVideoCyan,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Re-roll Seed",
            color = ReVideoCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        project.seeds.forEachIndexed { index, seed ->
          val isActive = project.activeSeedIndex == index
          Box(
            modifier = Modifier
              .width(160.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(ReVideoSurface1)
              .border(
                1.dp,
                if (isActive) ReVideoPrimary else ReVideoBorderSubtle,
                RoundedCornerShape(12.dp)
              )
              .clickable { viewModel.setActiveSeed(index) }
              .padding(12.dp)
              .testTag("seed_card_$index")
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isActive) ReVideoPrimary.copy(alpha = 0.2f) else ReVideoSurface2)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = seed.label,
                    color = if (isActive) ReVideoPrimaryLight else ReVideoTextMedium,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                  )
                }

                Icon(
                  imageVector = if (isActive) Icons.Default.CheckCircle else Icons.Default.Sync,
                  contentDescription = null,
                  tint = if (isActive) ReVideoPrimaryLight else ReVideoTextSubtle,
                  modifier = Modifier.size(15.dp)
                )
              }

              Text(
                text = seed.name,
                color = ReVideoTextHigh,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = seed.code,
                color = ReVideoTextSubtle,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
