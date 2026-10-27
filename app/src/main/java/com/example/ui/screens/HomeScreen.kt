package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PresetCatalog
import com.example.data.model.VideoProject
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryDim
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurface3
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.ui.theme.ReVideoTextSubtle
import com.example.viewmodel.AppTab
import com.example.viewmodel.CreateStep
import com.example.viewmodel.ReVideoViewModel

@Composable
fun HomeScreen(
  viewModel: ReVideoViewModel,
  modifier: Modifier = Modifier
) {
  val projects by viewModel.projects.collectAsState()
  val videoMinsLeft by viewModel.videoMinsLeft.collectAsState()
  val diffusionParams by viewModel.diffusionParams.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Workspace Welcome & Status
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "WORKSPACE",
            color = ReVideoPrimaryLight,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
          )
          Text(
            text = "Welcome back, Alex",
            color = ReVideoTextHigh,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
          )
        }

        // Pro Active status pill
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorder, RoundedCornerShape(9999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(ReVideoEmerald)
          )
          Text(
            text = "Pro Active",
            color = ReVideoTextMedium,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Pro Plan Info
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(ReVideoSurface1)
          .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp))
          .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.HourglassTop,
          contentDescription = null,
          tint = ReVideoPrimaryLight,
          modifier = Modifier.size(15.dp)
        )
        Text(
          text = "Pro Plan • ",
          color = ReVideoTextMedium,
          fontSize = 12.sp
        )
        Text(
          text = "$videoMinsLeft Video Mins Left",
          color = ReVideoTextHigh,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    // 2. Hero Launcher Card: Sleek Matte Design
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorder, RoundedCornerShape(16.dp))
        .padding(16.dp)
        .testTag("hero_launcher_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ReVideoSurface2)
                .border(1.dp, ReVideoBorder, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "Video-to-Video",
                color = ReVideoPrimaryDim,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = "Gen-3 Turbo",
                color = ReVideoTextMedium,
                fontSize = 11.sp
              )
            }
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Start Video-to-Video Workflow",
            color = ReVideoTextHigh,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Transform existing clips with neural prompt guidance, style transfer & motion mapping.",
            color = ReVideoTextMedium,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
        }

        Button(
          onClick = {
            viewModel.selectTab(AppTab.CREATE)
            viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
          },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("upload_reference_video_hero_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.VideoCall,
              contentDescription = null,
              modifier = Modifier.size(19.dp)
            )
            Text(
              text = "Upload Reference Video",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // 3. 4-Step Process Guide
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
            imageVector = Icons.Default.Hub,
            contentDescription = null,
            tint = ReVideoPrimaryLight,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "How Video-to-Video Works",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
        Text(
          text = "4 Steps",
          color = ReVideoTextSubtle,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        WorkflowStepCard("1", Icons.Default.UploadFile, "Upload Video", "MP4 or MOV reference clip up to 60s at 1080p.")
        WorkflowStepCard("2", Icons.Default.Hub, "AI Deconstruct", "Optical scene flow, motion & camera vectors.")
        WorkflowStepCard("3", Icons.Default.Palette, "Combine & Prompt", "Prompt directives, cinematic seeds & lighting.")
        WorkflowStepCard("4", Icons.Default.MovieFilter, "Render Video", "High-fps neural coherent synthesis export.")
      }
    }

    // 4. Transfer Presets Chips
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Transfer Presets",
          color = ReVideoTextHigh,
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "View All (${PresetCatalog.allPresets.size})",
          color = ReVideoPrimaryLight,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.clickable {
            viewModel.showToast("All 18 presets synchronized with Gemini", "palette")
          }
        )
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        PresetCatalog.allPresets.forEach { preset ->
          val isSelected = diffusionParams.selectedPreset.contains(preset.name)
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(if (isSelected) ReVideoPrimary else ReVideoSurface1)
              .border(
                1.dp,
                if (isSelected) ReVideoPrimaryLight else ReVideoBorder,
                RoundedCornerShape(9999.dp)
              )
              .clickable {
                viewModel.selectPreset(preset.name)
                viewModel.updateDiffusionMotion(preset.motionFidelity)
                viewModel.updateDiffusionStyle(preset.styleOverwrite)
              }
              .padding(horizontal = 14.dp, vertical = 7.dp)
              .testTag("preset_${preset.id}"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            val icon = when (preset.id) {
              "style_transfer" -> Icons.Default.Brush
              "motion_retargeting" -> Icons.AutoMirrored.Filled.DirectionsRun
              "cinematic_anime" -> Icons.Default.Animation
              "cyberpunk" -> Icons.Default.ElectricBolt
              "claymation" -> Icons.Default.Draw
              else -> Icons.Default.AutoAwesome
            }
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = if (isSelected) Color.White else ReVideoTextMedium,
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = preset.name,
              color = if (isSelected) Color.White else ReVideoTextMedium,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }

    // 5. Recent Projects
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
            text = "Recent Projects",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(ReVideoSurface2)
              .border(1.dp, ReVideoBorder, RoundedCornerShape(9999.dp))
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "${projects.size} active",
              color = ReVideoTextMedium,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Row(
          modifier = Modifier.clickable {
            viewModel.selectTab(AppTab.HISTORY)
          },
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "History",
            color = ReVideoTextMedium,
            fontSize = 12.sp
          )
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ReVideoTextMedium,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      if (projects.isEmpty()) {
        // Empty state card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
            .padding(28.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ReVideoSurface2)
                .border(1.dp, ReVideoBorder, RoundedCornerShape(16.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.VideoLibrary,
                contentDescription = null,
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(28.dp)
              )
            }
            Text(
              text = "No projects yet",
              color = ReVideoTextHigh,
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Upload a reference video to analyze its motion, timing, and style, then synthesize your first video-to-video project.",
              color = ReVideoTextMedium,
              fontSize = 13.sp,
              lineHeight = 18.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
              onClick = {
                viewModel.selectTab(AppTab.CREATE)
                viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
              },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .height(44.dp)
                .testTag("upload_first_video_btn")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.VideoCall,
                  contentDescription = null,
                  modifier = Modifier.size(19.dp)
                )
                Text("Upload Your First Video", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      } else {
        // Populated projects list
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          projects.take(3).forEach { project ->
            ProjectCardItem(
              project = project,
              onClick = { viewModel.openProject(project) }
            )
          }
        }
      }
    }

    // 6. Floating Clean Bottom Action Button
    Button(
      onClick = {
        viewModel.selectTab(AppTab.CREATE)
        viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
      },
      colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("new_v2v_project_bottom_fab")
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.AddCircle,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Text(
          text = "New Video-to-Video Project",
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold
        )
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "-5 credits",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}

@Composable
private fun WorkflowStepCard(
  number: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  desc: String
) {
  Box(
    modifier = Modifier
      .width(200.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(ReVideoSurface1)
      .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
      .padding(14.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ReVideoSurface2),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = number,
            color = ReVideoPrimaryDim,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = ReVideoTextSubtle,
          modifier = Modifier.size(18.dp)
        )
      }

      Text(
        text = title,
        color = ReVideoTextHigh,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold
      )
      Text(
        text = desc,
        color = ReVideoTextMedium,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )
    }
  }
}

@Composable
fun ProjectCardItem(
  project: VideoProject,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(ReVideoSurface1)
      .border(1.dp, ReVideoBorder, RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .padding(14.dp)
      .testTag("project_card_${project.id}")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(ReVideoSurface2)
          .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.MovieFilter,
          contentDescription = null,
          tint = ReVideoCyan,
          modifier = Modifier.size(24.dp)
        )
      }

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = project.title,
            color = ReVideoTextHigh,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = project.timestamp,
            color = ReVideoTextSubtle,
            fontSize = 11.sp
          )
        }
        Text(
          text = project.synthesizedPrompt.replace("\"", ""),
          color = ReVideoTextMedium,
          fontSize = 12.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.padding(top = 2.dp)
        )
        Text(
          text = "${project.referenceClip.duration} • ${project.diffusionParameters.selectedPreset} • 1080p",
          color = ReVideoPrimaryLight,
          fontSize = 11.sp,
          modifier = Modifier.padding(top = 2.dp)
        )
      }
    }
  }
}
