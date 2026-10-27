package com.example.data.repository

import com.example.data.model.ReferenceClip
import com.example.data.model.VideoProject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProjectRepository {

  private val _credits = MutableStateFlow(45)
  val credits: StateFlow<Int> = _credits.asStateFlow()

  private val _videoMinsLeft = MutableStateFlow(120)
  val videoMinsLeft: StateFlow<Int> = _videoMinsLeft.asStateFlow()

  // Sample reference clips provided for studio exploration when no local videos are on device
  val sampleClips = listOf(
    ReferenceClip(
      id = "clip_parkour",
      name = "Urban Runner Parkour (Sample)",
      duration = "0:15",
      durationMs = 15000L,
      resolution = "1080p 60fps",
      motionStreamsCount = 1,
      isSampleClip = true,
      thumbnailTheme = "parkour"
    ),
    ReferenceClip(
      id = "clip_drone",
      name = "Cyberpunk Drone Flythrough (Sample)",
      duration = "0:12",
      durationMs = 12000L,
      resolution = "1080p 60fps",
      motionStreamsCount = 2,
      isSampleClip = true,
      thumbnailTheme = "drone"
    ),
    ReferenceClip(
      id = "clip_neon",
      name = "Neon Night Highway Drift (Sample)",
      duration = "0:18",
      durationMs = 18000L,
      resolution = "1080p 60fps",
      motionStreamsCount = 1,
      isSampleClip = true,
      thumbnailTheme = "neon"
    ),
    ReferenceClip(
      id = "clip_tokyo",
      name = "Shibuya Rain Alley Walk (Sample)",
      duration = "0:14",
      durationMs = 14000L,
      resolution = "1080p 60fps",
      motionStreamsCount = 1,
      isSampleClip = true,
      thumbnailTheme = "tokyo"
    )
  )

  // Strictly user-created projects — never populated with fictional/fake completed records
  private val _projects = MutableStateFlow<List<VideoProject>>(emptyList())
  val projects: StateFlow<List<VideoProject>> = _projects.asStateFlow()

  fun deductCredits(amount: Int): Boolean {
    if (_credits.value >= amount) {
      _credits.value -= amount
      return true
    }
    return false
  }

  fun refundCredits(amount: Int) {
    _credits.value += amount
  }

  fun addCredits(amount: Int) {
    _credits.value += amount
  }

  fun saveProject(project: VideoProject) {
    val current = _projects.value.toMutableList()
    val index = current.indexOfFirst { it.id == project.id }
    if (index >= 0) {
      current[index] = project
    } else {
      current.add(0, project)
    }
    _projects.value = current
  }

  fun getProject(id: String): VideoProject? {
    return _projects.value.find { it.id == id }
  }

  fun toggleFavorite(id: String) {
    _projects.value = _projects.value.map {
      if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
    }
  }

  fun deleteProject(id: String) {
    _projects.value = _projects.value.filterNot { it.id == id }
  }

  fun updateActiveSeed(projectId: String, seedIndex: Int) {
    _projects.value = _projects.value.map {
      if (it.id == projectId) it.copy(activeSeedIndex = seedIndex) else it
    }
  }
}
