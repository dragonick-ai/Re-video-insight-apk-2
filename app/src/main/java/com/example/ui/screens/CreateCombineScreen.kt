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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoAspectRatio
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoError
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurface3
import com.example.ui.theme.ReVideoSurfaceLowest
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.ui.theme.ReVideoTextSubtle
import com.example.viewmodel.CreateStep
import com.example.viewmodel.ReVideoViewModel

@Composable
fun CreateCombineScreen(
  viewModel: ReVideoViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val selectedClip by viewModel.selectedClip.collectAsState()
  val userIdea by viewModel.userIdea.collectAsState()
  val isEnhancing by viewModel.isEnhancingIdea.collectAsState()
  val diffusionParams by viewModel.diffusionParams.collectAsState()
  val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()
  val combinedPrompt by viewModel.combinedPromptBlueprint.collectAsState()
  val isGenerating by viewModel.isGenerating.collectAsState()
  val generationStatusText by viewModel.generationStatusText.collectAsState()
  val generationProgress by viewModel.generationProgress.collectAsState()
  val generationError by viewModel.generationErrorMessage.collectAsState()
  val credits by viewModel.credits.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Stepper Indicator
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(14.dp)
        .testTag("combine_stepper_card")
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Step 1: Analyzed
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.clickable {
            viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
          }
        ) {
          Box(
            modifier = Modifier
              .size(20.dp)
              .clip(CircleShape)
              .background(ReVideoPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(13.dp)
            )
          }
          Text(text = "Analyzed", color = ReVideoTextHigh, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .padding(horizontal = 8.dp)
            .background(ReVideoPrimary.copy(alpha = 0.4f))
        )

        // Step 2: Combine Idea (Active)
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
            Text(text = "2", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          Text(text = "Combine Idea", color = ReVideoTextHigh, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .padding(horizontal = 8.dp)
            .background(ReVideoSurface2)
        )

        // Step 3: Generate
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
          Text(text = "Generate", color = ReVideoTextMedium, fontSize = 12.sp)
        }
      }
    }

    // 2. Source Clip Lock Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(12.dp)
        .testTag("source_clip_lock_banner")
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Thumbnail with lock
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                Brush.linearGradient(listOf(Color(0xFF4A2A1A), Color(0xFF1E212A)))
              ),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = selectedClip?.name?.take(22) ?: "Active Reference Clip",
                color = ReVideoTextHigh,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(ReVideoSurface2)
                  .padding(horizontal = 6.dp, vertical = 1.dp)
              ) {
                Text(
                  text = selectedClip?.duration ?: "0:15",
                  color = ReVideoTextMedium,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace
                )
              }
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier.padding(top = 2.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = null,
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = if (!selectedClip?.videoBase64.isNullOrBlank()) {
                  "Inline Reference Video (${selectedClip?.mimeType}) Attached"
                } else {
                  "1 Motion Stream Attached"
                },
                color = ReVideoTextMedium,
                fontSize = 11.sp
              )
            }
          }
        }

        IconButton(
          onClick = {
            viewModel.showToast("Reference video motion stream & base prompt bound", "tune")
          },
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(ReVideoSurface2)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp))
        ) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = "Tune",
            tint = ReVideoTextMedium,
            modifier = Modifier.size(17.dp)
          )
        }
      }
    }

    // 3. Creative Idea Input Section
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(14.dp)
        .testTag("creative_idea_section")
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
            Icon(
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Your Creative Idea",
              color = ReVideoTextHigh,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          // AI Enhance Button
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(ReVideoPrimary.copy(alpha = 0.15f))
              .border(1.dp, ReVideoPrimary.copy(alpha = 0.3f), RoundedCornerShape(9999.dp))
              .clickable { viewModel.triggerAiEnhance() }
              .padding(horizontal = 10.dp, vertical = 4.dp)
              .testTag("ai_enhance_btn"),
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
              text = if (isEnhancing) "Enhancing..." else "AI Enhance",
              color = ReVideoPrimaryLight,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Outlined Input Shell
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ReVideoSurfaceLowest)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp))
            .padding(10.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
              value = userIdea,
              onValueChange = { if (it.length <= 500) viewModel.updateUserIdea(it) },
              placeholder = {
                Text(
                  text = "Describe how you want to transform this video... (e.g. style, lighting, character changes, visual effects, environment)",
                  color = ReVideoTextMedium.copy(alpha = 0.5f),
                  fontSize = 13.sp,
                  lineHeight = 18.sp
                )
              },
              colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = ReVideoTextHigh,
                unfocusedTextColor = ReVideoTextHigh
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("creative_idea_input")
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${userIdea.length} / 500 chars",
                color = ReVideoTextSubtle,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )

              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                  onClick = {
                    viewModel.updateUserIdea(
                      "Futuristic cyberpunk athlete with glowing cobalt trails, vaulting across neon skyscraper scaffolds in volumetric rain"
                    )
                    viewModel.showToast("Voice transcription applied", "mic")
                  },
                  modifier = Modifier.size(28.dp).testTag("voice_input_btn")
                ) {
                  Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice input",
                    tint = ReVideoTextMedium,
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = { viewModel.updateUserIdea("") },
                  modifier = Modifier.size(28.dp).testTag("clear_text_btn")
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Clear text",
                    tint = ReVideoTextMedium,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // 4. Diffusion Parameters Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(14.dp)
        .testTag("diffusion_parameters_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Diffusion Parameters",
            color = ReVideoTextHigh,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(ReVideoSurface2)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "Neural V2V Mode",
              color = ReVideoTextMedium,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Slider 1: Reference Motion Fidelity
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Reference Motion Fidelity",
              color = ReVideoTextHigh,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "${diffusionParams.motionFidelity}%",
              color = ReVideoPrimaryLight,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          Slider(
            value = diffusionParams.motionFidelity.toFloat(),
            onValueChange = { viewModel.updateDiffusionMotion(it.toInt()) },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
              thumbColor = ReVideoPrimary,
              activeTrackColor = ReVideoPrimary,
              inactiveTrackColor = ReVideoSurface2
            ),
            modifier = Modifier.fillMaxWidth().testTag("motion_fidelity_slider")
          )

          Text(
            text = "Keeps exact original movements and camera dolly trajectory.",
            color = ReVideoTextSubtle,
            fontSize = 11.sp
          )
        }

        // Slider 2: Visual Style Overwrite
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Visual Style Overwrite",
              color = ReVideoTextHigh,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "${diffusionParams.styleOverwrite}%",
              color = ReVideoPrimaryLight,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          Slider(
            value = diffusionParams.styleOverwrite.toFloat(),
            onValueChange = { viewModel.updateDiffusionStyle(it.toInt()) },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
              thumbColor = ReVideoPrimary,
              activeTrackColor = ReVideoPrimary,
              inactiveTrackColor = ReVideoSurface2
            ),
            modifier = Modifier.fillMaxWidth().testTag("style_overwrite_slider")
          )

          Text(
            text = "Restyles textures to ${diffusionParams.selectedPreset}.",
            color = ReVideoTextSubtle,
            fontSize = 11.sp
          )
        }

        // Output Aspect Ratio Picker (16:9 or 9:16)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
            text = "OUTPUT ASPECT RATIO",
            color = ReVideoTextSubtle,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(
              VideoAspectRatio.LANDSCAPE to "16:9 Landscape",
              VideoAspectRatio.PORTRAIT to "9:16 Portrait"
            ).forEach { (ratio, label) ->
              val isSelected = selectedAspectRatio == ratio
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) ReVideoPrimary.copy(alpha = 0.25f) else ReVideoSurface2)
                  .border(
                    1.dp,
                    if (isSelected) ReVideoPrimaryLight else ReVideoBorderSubtle,
                    RoundedCornerShape(8.dp)
                  )
                  .clickable { viewModel.setAspectRatio(ratio) }
                  .padding(horizontal = 10.dp, vertical = 7.dp)
                  .testTag("combine_ratio_${ratio.name}"),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ReVideoPrimaryLight,
                    modifier = Modifier.size(13.dp)
                  )
                }
                Text(
                  text = label,
                  color = if (isSelected) ReVideoPrimaryLight else ReVideoTextMedium,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(start = if (isSelected) 4.dp else 0.dp)
                )
              }
            }
          }
        }

        // Style Presets Picker
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
            text = "STYLE PRESETS",
            color = ReVideoTextSubtle,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(
              "Cyberpunk Neo-Tokyo",
              "Dark Fantasy Anime",
              "Photorealistic 8K",
              "3D Claymation",
              "Synthwave 80s",
              "Steampunk"
            ).forEach { presetName ->
              val isSelected = diffusionParams.selectedPreset == presetName
              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) ReVideoCyan.copy(alpha = 0.2f) else ReVideoSurface2)
                  .border(
                    1.dp,
                    if (isSelected) ReVideoCyan else ReVideoBorderSubtle,
                    RoundedCornerShape(8.dp)
                  )
                  .clickable { viewModel.selectPreset(presetName) }
                  .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ReVideoCyan,
                    modifier = Modifier.size(13.dp)
                  )
                }
                Text(
                  text = presetName,
                  color = if (isSelected) ReVideoCyan else ReVideoTextMedium,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }

        // Switches / Checkboxes
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          // Checkbox 1: Preserve original background geometry
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(ReVideoSurface2)
              .clickable { viewModel.togglePreserveGeometry() }
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Apartment,
                contentDescription = null,
                tint = ReVideoTextMedium,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Preserve original background geometry",
                color = ReVideoTextHigh,
                fontSize = 12.sp
              )
            }
            Checkbox(
              checked = diffusionParams.preserveBackgroundGeometry,
              onCheckedChange = { viewModel.togglePreserveGeometry() },
              colors = CheckboxDefaults.colors(
                checkedColor = ReVideoPrimary,
                checkmarkColor = Color.White
              ),
              modifier = Modifier.size(20.dp)
            )
          }

          // Checkbox 2: Generate matching AI sound effects
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(ReVideoSurface2)
              .clickable { viewModel.toggleAiSoundEffects() }
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                tint = ReVideoTextMedium,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Generate matching AI sound effects",
                color = ReVideoTextHigh,
                fontSize = 12.sp
              )
            }
            Checkbox(
              checked = diffusionParams.generateAiSoundEffects,
              onCheckedChange = { viewModel.toggleAiSoundEffects() },
              colors = CheckboxDefaults.colors(
                checkedColor = ReVideoPrimary,
                checkmarkColor = Color.White
              ),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // 5. Final Combined Prompt Review & Edit Box
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(14.dp)
        .testTag("combined_prompt_box")
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
            Icon(
              imageVector = Icons.Default.Terminal,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Final Combined Prompt",
              color = ReVideoTextHigh,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(ReVideoSurface2)
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = if (userIdea.isNotBlank() || !combinedPrompt.contains("Your combined prompt")) "Editable Blueprint" else "Awaiting Input",
              color = if (userIdea.isNotBlank() || !combinedPrompt.contains("Your combined prompt")) ReVideoCyan else ReVideoTextMedium,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ReVideoSurfaceLowest)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp))
            .padding(4.dp)
        ) {
          TextField(
            value = combinedPrompt,
            onValueChange = { viewModel.updateCombinedPromptBlueprint(it) },
            colors = TextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              disabledContainerColor = Color.Transparent,
              focusedIndicatorColor = Color.Transparent,
              unfocusedIndicatorColor = Color.Transparent,
              focusedTextColor = ReVideoTextHigh,
              unfocusedTextColor = ReVideoTextHigh
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("combined_prompt_input")
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          Button(
            onClick = { viewModel.generatePromptBlueprint() },
            colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(34.dp).testTag("generate_blueprint_btn")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(14.dp)
              )
              Text("Generate Prompt Blueprint", color = ReVideoPrimaryLight, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // 6. Model Capability & Validation Notice
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(12.dp)
        .testTag("capability_validation_card")
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ReVideoSurface2),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Memory,
            contentDescription = null,
            tint = ReVideoPrimaryLight,
            modifier = Modifier.size(16.dp)
          )
        }
        Text(
          text = "Gemini Omni 1.1 Flash (Interactions API) • Sends reference video stream + combined prompt (${selectedAspectRatio.label}). Requires active video-generation API access; never substitutes text-only or fake renders.",
          color = ReVideoTextMedium,
          fontSize = 11.sp,
          lineHeight = 15.sp
        )
      }
    }

    // Generation Progress (if actively synthesizing)
    if (isGenerating) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ReVideoSurfaceLowest)
          .border(1.dp, ReVideoPrimary, RoundedCornerShape(12.dp))
          .padding(14.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = generationStatusText,
              color = ReVideoPrimaryLight,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "$generationProgress%",
              color = ReVideoCyan,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp))
              .background(ReVideoSurface2)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth((generationProgress / 100f).coerceIn(0f, 1f))
                .height(6.dp)
                .background(ReVideoPrimary)
            )
          }
        }
      }
    }

    // Generation Error / Setup Notice
    if (generationError != null) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF2B1619))
          .border(1.dp, ReVideoError.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
          .padding(14.dp)
          .testTag("generation_error_card")
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ReVideoError, modifier = Modifier.size(18.dp))
            Text("Video Generation Service Notice", color = ReVideoError, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }
          Text(
            text = generationError ?: "",
            color = ReVideoTextHigh,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    // 7. Sticky Bottom Navigation Bar & Generation Dock
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(ReVideoSurface2)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(14.dp))
        .padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = ReVideoPrimaryLight,
            modifier = Modifier.size(15.dp)
          )
          Text(text = "Generation Cost: ", color = ReVideoTextMedium, fontSize = 11.sp)
          Text(text = "10 Credits", color = ReVideoTextHigh, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(text = "Balance: ", color = ReVideoTextMedium, fontSize = 11.sp)
          Text(text = "$credits Credits", color = ReVideoPrimaryLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = {
            viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
          },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface3),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.height(44.dp).testTag("combine_back_btn")
        ) {
          Text("Back", color = ReVideoTextHigh, fontSize = 13.sp)
        }

        Button(
          onClick = {
            viewModel.startVideoGeneration(outputDir = context.filesDir)
          },
          enabled = !isGenerating,
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("generate_video_primary_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = if (isGenerating) "Generating (Interactions API)..." else "Generate Video (10 Credits)",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}
