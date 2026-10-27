package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.data.model.VideoAspectRatio
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurface3
import com.example.ui.theme.ReVideoSurfaceLowest
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.ui.theme.ReVideoTextSubtle
import com.example.viewmodel.ReVideoViewModel

@Composable
fun SettingsScreen(
  viewModel: ReVideoViewModel,
  modifier: Modifier = Modifier
) {
  val credits by viewModel.credits.collectAsState()
  val videoMinsLeft by viewModel.videoMinsLeft.collectAsState()
  val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()

  var is60FpsSmoothing by remember { mutableStateOf(true) }
  var isHardwareAccel by remember { mutableStateOf(true) }
  var isHighTensorPrecision by remember { mutableStateOf(true) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Studio Profile Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(16.dp)
        .testTag("settings_profile_card")
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(ReVideoPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "A", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
          }

          Column {
            Text(text = "Alex", color = ReVideoTextHigh, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "dragonickplayer@gmail.com", color = ReVideoTextMedium, fontSize = 12.sp)
            Row(
              modifier = Modifier.padding(top = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ReVideoEmerald))
              Text(text = "Pro Active", color = ReVideoEmerald, fontSize = 11.sp, fontWeight = FontWeight.Medium)
              Text(text = "•", color = ReVideoTextSubtle, fontSize = 11.sp)
              Text(text = "$videoMinsLeft Mins Left", color = ReVideoTextMedium, fontSize = 11.sp)
            }
          }
        }

        Button(
          onClick = { viewModel.setCreditDialogVisible(true) },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
          border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorderSubtle),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.height(36.dp).testTag("top_up_credits_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = ReVideoPrimaryLight, modifier = Modifier.size(15.dp))
            Text("Top Up", color = ReVideoPrimaryLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }

    // 2. AI Model Configurations (Argon & Omni)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(16.dp)
        .testTag("ai_engine_config_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Psychology, contentDescription = null, tint = ReVideoCyan, modifier = Modifier.size(20.dp))
          Text(text = "AI Model Engine Pipeline", color = ReVideoTextHigh, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        // Analysis Model Item
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ReVideoSurface2)
            .padding(12.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Video Understanding Model", color = ReVideoTextHigh, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(ReVideoPrimary.copy(alpha = 0.2f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(text = "ACTIVE", color = ReVideoPrimaryLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
            Text(
              text = "gemini-4-argon",
              color = ReVideoCyan,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "Extracts optical flow, camera velocity, lighting, and scene decomposition layers.",
              color = ReVideoTextMedium,
              fontSize = 11.sp,
              lineHeight = 15.sp
            )
          }
        }

        // Video Generation Model Item
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ReVideoSurface2)
            .padding(12.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Video Generation Model", color = ReVideoTextHigh, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(ReVideoCyan.copy(alpha = 0.2f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(text = "INTERACTIONS API", color = ReVideoCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
            Text(
              text = "gemini-omni-1.1-flash",
              color = ReVideoPrimaryLight,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "Multi-pass video-to-video synthesis at 60fps with temporal motion consistency.",
              color = ReVideoTextMedium,
              fontSize = 11.sp,
              lineHeight = 15.sp
            )
          }
        }

        // Aspect Ratio Selector
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(text = "DEFAULT ASPECT RATIO", color = ReVideoTextSubtle, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(
              VideoAspectRatio.LANDSCAPE to "16:9 Landscape",
              VideoAspectRatio.PORTRAIT to "9:16 Portrait"
            ).forEach { (ratio, label) ->
              val isSelected = selectedAspectRatio == ratio
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(38.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) ReVideoPrimary else ReVideoSurface2)
                  .border(
                    1.dp,
                    if (isSelected) ReVideoPrimaryLight else ReVideoBorderSubtle,
                    RoundedCornerShape(10.dp)
                  )
                  .clickable { viewModel.setAspectRatio(ratio) }
                  .testTag("ratio_${ratio.name}"),
                contentAlignment = Alignment.Center
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                  }
                  Text(
                    text = label,
                    color = if (isSelected) Color.White else ReVideoTextMedium,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Studio Engine & Hardware Acceleration
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Speed, contentDescription = null, tint = ReVideoPrimaryLight, modifier = Modifier.size(20.dp))
          Text(text = "Rendering Preferences", color = ReVideoTextHigh, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "60 FPS Neural Smoothing", color = ReVideoTextHigh, fontSize = 13.sp)
            Text(text = "Interpolates frame vectors for fluid playback", color = ReVideoTextMedium, fontSize = 11.sp)
          }
          Switch(
            checked = is60FpsSmoothing,
            onCheckedChange = { is60FpsSmoothing = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ReVideoPrimary)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "GPU Hardware Acceleration", color = ReVideoTextHigh, fontSize = 13.sp)
            Text(text = "Utilizes Vulkan / OpenCL on-device pipelines", color = ReVideoTextMedium, fontSize = 11.sp)
          }
          Switch(
            checked = isHardwareAccel,
            onCheckedChange = { isHardwareAccel = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ReVideoPrimary)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "High Precision Optical Flow", color = ReVideoTextHigh, fontSize = 13.sp)
            Text(text = "Deep motion vector matrix calculation", color = ReVideoTextMedium, fontSize = 11.sp)
          }
          Switch(
            checked = isHighTensorPrecision,
            onCheckedChange = { isHighTensorPrecision = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ReVideoPrimary)
          )
        }
      }
    }

    // 4. API Key, Capability Validation & Security Credentials
    val capabilityReport = viewModel.getCapabilityValidationReport()
    val isKeyReady = capabilityReport.isApiKeyConfigured
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(16.dp)
        .testTag("settings_api_credentials_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            Icons.Default.Key,
            contentDescription = null,
            tint = if (isKeyReady) ReVideoEmerald else ReVideoCyan,
            modifier = Modifier.size(20.dp)
          )
          Text(
            text = "API Key & Capability Validation",
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Text(
          text = "Gemini API access is secured via BuildConfig.GEMINI_API_KEY (Secrets Gradle Plugin). Configure your key in the AI Studio Secrets panel — private keys are never hardcoded.",
          color = ReVideoTextMedium,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ReVideoSurfaceLowest)
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isKeyReady) ReVideoEmerald else ReVideoCyan)
            )
            Text(text = "GEMINI_API_KEY", color = ReVideoTextHigh, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          }
          Text(
            text = if (isKeyReady) "Active (BuildConfig)" else "Setup Required (Secrets Panel)",
            color = if (isKeyReady) ReVideoEmerald else ReVideoCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ReVideoSurfaceLowest)
            .padding(10.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "VALIDATED CAPABILITIES & LIMITS",
              color = ReVideoTextSubtle,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = "• Inputs: ${capabilityReport.supportedInputMimeTypes.joinToString(", ")} (up to ${capabilityReport.maxRecommendedDurationSec}s / ${capabilityReport.maxInlineVideoSizeMb}MB inline)\n• Outputs: ${capabilityReport.supportedOutputFormats.joinToString(", ")}\n• ${capabilityReport.transformationLimitationNote}",
              color = ReVideoTextMedium,
              fontSize = 11.sp,
              lineHeight = 16.sp
            )
          }
        }
      }
    }

    // 5. Storage & Diagnostics
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Storage, contentDescription = null, tint = ReVideoTextMedium, modifier = Modifier.size(20.dp))
          Text(text = "Cache & Local Data", color = ReVideoTextHigh, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Video Buffer Cache", color = ReVideoTextMedium, fontSize = 13.sp)
          Text(text = "34.8 MB", color = ReVideoTextHigh, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
        }

        Button(
          onClick = { viewModel.showToast("Cache cleared successfully", "check_circle") },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
          border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorderSubtle),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.CleanHands, contentDescription = null, tint = ReVideoTextMedium, modifier = Modifier.size(16.dp))
            Text("Clear Temporary Cache", color = ReVideoTextHigh, fontSize = 12.sp)
          }
        }
      }
    }

    // 6. About App
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurfaceLowest)
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "ReVideo AI - Neural Video Studio", color = ReVideoTextHigh, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          Text(text = "Google Stitch Design System Native Android App", color = ReVideoTextMedium, fontSize = 11.sp)
        }
        Text(text = "v2.4.1", color = ReVideoPrimaryLight, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
