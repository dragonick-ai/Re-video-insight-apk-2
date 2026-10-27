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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectStatus
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoOutline
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryContainer
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurface3
import com.example.ui.theme.ReVideoSurfaceLowest
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.ui.theme.ReVideoTextSubtle
import com.example.viewmodel.AppTab
import com.example.viewmodel.CreateStep
import com.example.viewmodel.HistoryFilterTab
import com.example.viewmodel.ProjectStatusFilter
import com.example.viewmodel.ReVideoViewModel

@Composable
fun HistoryScreen(
  viewModel: ReVideoViewModel,
  modifier: Modifier = Modifier
) {
  val projects by viewModel.projects.collectAsState()
  val historyTab by viewModel.historyTab.collectAsState()
  val statusFilter by viewModel.statusFilter.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val isSelectMode by viewModel.isSelectMode.collectAsState()
  val selectedIds by viewModel.selectedProjectIds.collectAsState()

  val filteredProjects = projects.filter { project ->
    val matchesTab = when (historyTab) {
      HistoryFilterTab.ALL -> true
      HistoryFilterTab.FAVORITES -> project.isFavorite
      HistoryFilterTab.CLIPS -> true
    }
    val matchesStatus = when (statusFilter) {
      ProjectStatusFilter.ALL -> true
      ProjectStatusFilter.COMPLETED -> project.status == ProjectStatus.COMPLETED
      ProjectStatusFilter.PROCESSING -> project.status == ProjectStatus.PROCESSING
      ProjectStatusFilter.DRAFTS -> project.status == ProjectStatus.DRAFT
      ProjectStatusFilter.FAILED -> project.status == ProjectStatus.FAILED
    }
    val matchesSearch = searchQuery.isBlank() ||
      project.title.contains(searchQuery, ignoreCase = true) ||
      project.synthesizedPrompt.contains(searchQuery, ignoreCase = true) ||
      project.userIdea.contains(searchQuery, ignoreCase = true)

    matchesTab && matchesStatus && matchesSearch
  }

  val favoritesCount = projects.count { it.isFavorite }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Dynamic Search & Selection Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .height(44.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(ReVideoSurface1)
          .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
          .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = ReVideoOutline,
            modifier = Modifier.size(20.dp)
          )
          TextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = {
              Text(
                "Search generations, seeds, prompts...",
                color = ReVideoOutline,
                fontSize = 12.sp,
                maxLines = 1
              )
            },
            colors = TextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              focusedIndicatorColor = Color.Transparent,
              unfocusedIndicatorColor = Color.Transparent,
              focusedTextColor = ReVideoTextHigh,
              unfocusedTextColor = ReVideoTextHigh
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("history_search_input")
          )
          IconButton(
            onClick = {
              viewModel.showToast("Filter parameters applied", "tune")
            },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Filter",
              tint = ReVideoOutline,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Select Mode Button
      Button(
        onClick = { viewModel.toggleSelectMode() },
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isSelectMode) ReVideoPrimaryContainer else ReVideoSurface1
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorderSubtle),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.height(44.dp).testTag("select_mode_btn")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Checklist,
            contentDescription = null,
            tint = if (isSelectMode) Color.White else ReVideoTextMedium,
            modifier = Modifier.size(17.dp)
          )
          Text(
            text = if (isSelectMode) "Cancel" else "Select",
            color = if (isSelectMode) Color.White else ReVideoTextMedium,
            fontSize = 12.sp
          )
        }
      }
    }

    // 2. Segmented View Controller Tabs
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurfaceLowest)
        .padding(4.dp)
        .testTag("history_tab_row")
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        listOf(
          Triple(HistoryFilterTab.ALL, "All (${projects.size})", "tab_all"),
          Triple(HistoryFilterTab.FAVORITES, "Favorites ($favoritesCount)", "tab_favorites"),
          Triple(HistoryFilterTab.CLIPS, "Clips (${viewModel.sampleClips.size})", "tab_clips")
        ).forEach { (tab, label, testTag) ->
          val isSelected = historyTab == tab
          Box(
            modifier = Modifier
              .weight(1f)
              .height(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) ReVideoPrimaryContainer else Color.Transparent)
              .clickable { viewModel.setHistoryTab(tab) }
              .testTag(testTag),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = label,
              color = if (isSelected) Color.White else ReVideoTextMedium,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
          }
        }
      }
    }

    // 3. Filter, Sort & Storage Status Sub-bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
        .padding(12.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Sort Dropdown Pill
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ReVideoSurface2)
              .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(8.dp))
              .clickable {
                viewModel.showToast("Sorted by Newest First", "swap_vert")
              }
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SwapVert,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = "Newest First",
              color = ReVideoTextHigh,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
            Icon(
              imageVector = Icons.Default.ExpandMore,
              contentDescription = null,
              tint = ReVideoOutline,
              modifier = Modifier.size(14.dp)
            )
          }

          // Cloud Quota Counter
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(text = "Storage", color = ReVideoOutline, fontSize = 11.sp)
            Text(
              text = "${projects.size} / 50 Slots",
              color = ReVideoTextHigh,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        // Storage Visual Progress Meter
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(ReVideoSurface3)
          ) {
            val ratio = (projects.size / 50f).coerceIn(0.04f, 1f)
            Box(
              modifier = Modifier
                .fillMaxWidth(ratio)
                .height(4.dp)
                .background(ReVideoPrimaryContainer)
            )
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "${(projects.size * 18.4).toInt()} MB utilized", color = ReVideoOutline, fontSize = 10.sp)
            Text(text = "5.0 GB Cloud Allocation", color = ReVideoOutline, fontSize = 10.sp)
          }
        }

        // Status Horizontal Filter Pills
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            ProjectStatusFilter.ALL to "All",
            ProjectStatusFilter.COMPLETED to "Completed",
            ProjectStatusFilter.PROCESSING to "Processing",
            ProjectStatusFilter.DRAFTS to "Drafts",
            ProjectStatusFilter.FAILED to "Failed"
          ).forEach { (filter, label) ->
            val isSelected = statusFilter == filter
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(if (isSelected) ReVideoPrimaryContainer else ReVideoSurface2)
                .clickable { viewModel.setStatusFilter(filter) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                color = if (isSelected) Color.White else ReVideoTextMedium,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
              )
            }
          }
        }
      }
    }

    // Selection Action Bar if in Select Mode
    if (isSelectMode && selectedIds.isNotEmpty()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(ReVideoSurface3)
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${selectedIds.size} Selected",
          color = ReVideoTextHigh,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
        Button(
          onClick = { viewModel.deleteSelectedProjects() },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93000A)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.height(32.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
            Text("Delete Selected", fontSize = 11.sp)
          }
        }
      }
    }

    // 4. Primary Dynamic Content Area
    if (historyTab == HistoryFilterTab.CLIPS) {
      // Clips list
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        viewModel.sampleClips.forEach { clip ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(ReVideoSurface1)
              .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
              .clickable {
                viewModel.selectClip(clip)
                viewModel.selectTab(AppTab.CREATE)
                viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
              }
              .padding(14.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(ReVideoSurface2),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Videocam,
                  contentDescription = null,
                  tint = ReVideoCyan,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(text = clip.name, color = ReVideoTextHigh, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                  text = "${clip.duration} • ${clip.resolution} • ${clip.motionStreamsCount} Motion Stream",
                  color = ReVideoTextMedium,
                  fontSize = 11.sp
                )
              }
              Button(
                onClick = {
                  viewModel.selectClip(clip)
                  viewModel.selectTab(AppTab.CREATE)
                  viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp)
              ) {
                Text("Use Clip", fontSize = 11.sp, color = ReVideoCyan)
              }
            }
          }
        }
      }
    } else if (filteredProjects.isEmpty()) {
      if (historyTab == HistoryFilterTab.FAVORITES) {
        // Empty Favorites State
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(ReVideoSurface2),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = null,
                tint = ReVideoCyan,
                modifier = Modifier.size(28.dp)
              )
            }
            Text(
              text = "No saved blueprints",
              color = ReVideoTextHigh,
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Pin outstanding seed variations, camera prompt templates, and favored visual style embeddings for instant re-use.",
              color = ReVideoTextMedium,
              fontSize = 12.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              modifier = Modifier.padding(horizontal = 14.dp)
            )
            Button(
              onClick = { viewModel.showToast("Browsing Studio Presets", "explore") },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.height(38.dp).padding(top = 4.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(15.dp))
                Text("Browse Studio Presets", color = ReVideoTextHigh, fontSize = 12.sp)
              }
            }
          }
        }
      } else {
        // Main Empty State: All Generations
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
            .padding(26.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ReVideoSurface2)
                .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MovieFilter,
                contentDescription = null,
                tint = ReVideoPrimaryLight,
                modifier = Modifier.size(32.dp)
              )
            }

            Text(
              text = "No generation history yet",
              color = ReVideoTextHigh,
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold
            )

            Text(
              text = "Your neural video-to-video renders, analyzed source camera trajectories, and custom blueprints will live here.",
              color = ReVideoTextMedium,
              fontSize = 12.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              modifier = Modifier.padding(horizontal = 14.dp)
            )

            Button(
              onClick = {
                viewModel.selectTab(AppTab.CREATE)
                viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
              },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimaryContainer),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(44.dp)
                .testTag("start_first_generation_history_btn")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("Start First Generation", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              }
            }

            Button(
              onClick = {
                viewModel.selectTab(AppTab.CREATE)
                viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
              },
              colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface2),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(40.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.UploadFile, contentDescription = null, tint = ReVideoTextMedium, modifier = Modifier.size(16.dp))
                Text("Import Reference Video", color = ReVideoTextMedium, fontSize = 12.sp)
              }
            }
          }
        }
      }
    } else {
      // Populated project items list
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        filteredProjects.forEach { project ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(ReVideoSurface1)
              .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(14.dp))
              .clickable {
                if (isSelectMode) {
                  viewModel.toggleProjectSelection(project.id)
                } else {
                  viewModel.openProject(project)
                }
              }
              .padding(12.dp)
              .testTag("history_item_${project.id}")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelectMode) {
                Checkbox(
                  checked = selectedIds.contains(project.id),
                  onCheckedChange = { viewModel.toggleProjectSelection(project.id) },
                  colors = CheckboxDefaults.colors(checkedColor = ReVideoPrimary)
                )
              }

              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(ReVideoSurface2),
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
                    fontSize = 13.sp,
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
                  fontSize = 11.sp,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.padding(top = 2.dp)
                )
                Row(
                  modifier = Modifier.padding(top = 4.dp),
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${project.referenceClip.duration} • ${project.diffusionParameters.selectedPreset}",
                    color = ReVideoPrimaryLight,
                    fontSize = 10.sp
                  )
                  Text(
                    text = "${project.creditsUsed} Credits",
                    color = ReVideoTextSubtle,
                    fontSize = 10.sp
                  )
                }
              }

              IconButton(
                onClick = { viewModel.toggleFavorite(project.id) },
                modifier = Modifier.size(30.dp)
              ) {
                Icon(
                  imageVector = if (project.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                  contentDescription = "Favorite",
                  tint = if (project.isFavorite) ReVideoEmerald else ReVideoTextSubtle,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }

    // 5. Informational Workflow Blueprint Guide
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ReVideoSurface1)
        .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(16.dp))
        .padding(14.dp)
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
            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = ReVideoPrimaryLight, modifier = Modifier.size(18.dp))
            Text("Neural Studio Workflow", color = ReVideoTextHigh, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }
          Text("4 Simple Steps", color = ReVideoOutline, fontSize = 11.sp)
        }

        listOf(
          Triple("1", "Upload Reference Video", "Supply source footage (MP4, MOV up to 60s) for kinetics and lighting breakdown."),
          Triple("2", "Kinetic & Optical Deconstruction", "Neural tracking extracts 3D camera translation, focal pans, and depth maps."),
          Triple("3", "Apply Neural Aesthetic Prompts", "Infuse cybernetic textures, volumetric atmospheric grain, or custom LoRA tokens."),
          Triple("4", "Render & Archive", "Your final multi-pass export archives here with persistent seed and parameter parity.")
        ).forEach { (stepNum, stepTitle, stepDesc) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(ReVideoSurface2.copy(alpha = 0.6f))
              .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
          ) {
            Box(
              modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ReVideoSurface3),
              contentAlignment = Alignment.Center
            ) {
              Text(stepNum, color = ReVideoPrimaryLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Column {
              Text(stepTitle, color = ReVideoTextHigh, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              Text(stepDesc, color = ReVideoOutline, fontSize = 11.sp, lineHeight = 15.sp)
            }
          }
        }
      }
    }

    // 6. Cloud Synchronization Footer
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ReVideoSurfaceLowest)
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Default.CloudDone, contentDescription = null, tint = ReVideoEmerald, modifier = Modifier.size(15.dp))
          Text("Cloud vault live • Encrypted archive", color = ReVideoOutline, fontSize = 11.sp)
        }
        Text("v2.4.1", color = ReVideoOutline, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
      }
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}
