package com.example.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CapabilityValidationReport
import com.example.data.model.DiffusionParameters
import com.example.data.model.ProjectStatus
import com.example.data.model.ReferenceClip
import com.example.data.model.VideoAspectRatio
import com.example.data.model.VideoProject
import com.example.data.repository.GeminiVideoService
import com.example.data.repository.ProjectRepository
import com.example.data.repository.ServiceResult
import com.example.data.repository.VideoAnalysisResult
import com.example.data.util.VideoMetadataHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

enum class AppTab(val route: String, val label: String) {
  HOME("home", "Home"),
  CREATE("create", "Create"),
  HISTORY("history", "History"),
  SETTINGS("settings", "Settings")
}

enum class CreateStep(val stepNumber: Int, val label: String) {
  UPLOAD_ANALYZE(1, "Upload & Analyze"),
  COMBINE_IDEA(2, "Combine Idea"),
  GENERATE(3, "Generate")
}

enum class HistoryFilterTab {
  ALL,
  FAVORITES,
  CLIPS
}

enum class ProjectStatusFilter {
  ALL,
  COMPLETED,
  PROCESSING,
  DRAFTS,
  FAILED
}

data class ToastEvent(
  val message: String,
  val iconName: String = "check_circle",
  val timestamp: Long = System.currentTimeMillis()
)

class ReVideoViewModel(
  private val repository: ProjectRepository = ProjectRepository(),
  private val geminiService: GeminiVideoService = GeminiVideoService()
) : ViewModel() {

  val credits: StateFlow<Int> = repository.credits
  val videoMinsLeft: StateFlow<Int> = repository.videoMinsLeft
  val projects: StateFlow<List<VideoProject>> = repository.projects
  val sampleClips = repository.sampleClips

  // Navigation state
  private val _currentTab = MutableStateFlow(AppTab.HOME)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  private val _createStep = MutableStateFlow(CreateStep.UPLOAD_ANALYZE)
  val createStep: StateFlow<CreateStep> = _createStep.asStateFlow()

  // Creation Studio State (starts with no clip selected - Awaiting Media)
  private val _selectedClip = MutableStateFlow<ReferenceClip?>(null)
  val selectedClip: StateFlow<ReferenceClip?> = _selectedClip.asStateFlow()

  private val _isAnalyzing = MutableStateFlow(false)
  val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

  private val _analysisProgress = MutableStateFlow(0)
  val analysisProgress: StateFlow<Int> = _analysisProgress.asStateFlow()

  private val _analysisStatusText = MutableStateFlow("Awaiting video upload")
  val analysisStatusText: StateFlow<String> = _analysisStatusText.asStateFlow()

  private val _analysisResult = MutableStateFlow<VideoAnalysisResult?>(null)
  val analysisResult: StateFlow<VideoAnalysisResult?> = _analysisResult.asStateFlow()

  // Editable Base Prompt generated from actual analysis results
  private val _editableBasePrompt = MutableStateFlow("")
  val editableBasePrompt: StateFlow<String> = _editableBasePrompt.asStateFlow()

  private val _analysisErrorMessage = MutableStateFlow<String?>(null)
  val analysisErrorMessage: StateFlow<String?> = _analysisErrorMessage.asStateFlow()

  // Step 2 State
  private val _userIdea = MutableStateFlow("")
  val userIdea: StateFlow<String> = _userIdea.asStateFlow()

  private val _isEnhancingIdea = MutableStateFlow(false)
  val isEnhancingIdea: StateFlow<Boolean> = _isEnhancingIdea.asStateFlow()

  private val _diffusionParams = MutableStateFlow(DiffusionParameters())
  val diffusionParams: StateFlow<DiffusionParameters> = _diffusionParams.asStateFlow()

  private val _combinedPromptBlueprint = MutableStateFlow(
    "Your combined prompt blueprint will appear here once you enter your creative idea. It will fuse reference camera motion, timing, and your style instructions."
  )
  val combinedPromptBlueprint: StateFlow<String> = _combinedPromptBlueprint.asStateFlow()

  // Step 3 & Active Project State
  private val _isGenerating = MutableStateFlow(false)
  val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

  private val _generationProgress = MutableStateFlow(0)
  val generationProgress: StateFlow<Int> = _generationProgress.asStateFlow()

  private val _generationStatusText = MutableStateFlow("")
  val generationStatusText: StateFlow<String> = _generationStatusText.asStateFlow()

  private val _generationErrorMessage = MutableStateFlow<String?>(null)
  val generationErrorMessage: StateFlow<String?> = _generationErrorMessage.asStateFlow()

  private val _activeProject = MutableStateFlow<VideoProject?>(null)
  val activeProject: StateFlow<VideoProject?> = _activeProject.asStateFlow()

  private val _selectedAspectRatio = MutableStateFlow(VideoAspectRatio.LANDSCAPE)
  val selectedAspectRatio: StateFlow<VideoAspectRatio> = _selectedAspectRatio.asStateFlow()

  // History Tab filtering & search
  private val _historyTab = MutableStateFlow(HistoryFilterTab.ALL)
  val historyTab: StateFlow<HistoryFilterTab> = _historyTab.asStateFlow()

  private val _statusFilter = MutableStateFlow(ProjectStatusFilter.ALL)
  val statusFilter: StateFlow<ProjectStatusFilter> = _statusFilter.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _isSelectMode = MutableStateFlow(false)
  val isSelectMode: StateFlow<Boolean> = _isSelectMode.asStateFlow()

  private val _selectedProjectIds = MutableStateFlow<Set<String>>(emptySet())
  val selectedProjectIds: StateFlow<Set<String>> = _selectedProjectIds.asStateFlow()

  // UI Dialogs & Toast
  private val _toastEvent = MutableStateFlow<ToastEvent?>(null)
  val toastEvent: StateFlow<ToastEvent?> = _toastEvent.asStateFlow()

  private val _showCreditDialog = MutableStateFlow(false)
  val showCreditDialog: StateFlow<Boolean> = _showCreditDialog.asStateFlow()

  private val _showNotificationDialog = MutableStateFlow(false)
  val showNotificationDialog: StateFlow<Boolean> = _showNotificationDialog.asStateFlow()

  private val _showProfileDialog = MutableStateFlow(false)
  val showProfileDialog: StateFlow<Boolean> = _showProfileDialog.asStateFlow()

  fun isApiKeyConfigured(): Boolean = geminiService.isApiKeyConfigured()

  fun getCapabilityValidationReport(): CapabilityValidationReport =
    geminiService.getCapabilityValidationReport()

  fun selectTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun setCreateStep(step: CreateStep) {
    _createStep.value = step
  }

  fun selectClip(clip: ReferenceClip) {
    _selectedClip.value = clip
    _analysisResult.value = null
    _editableBasePrompt.value = ""
    _analysisErrorMessage.value = null
    _analysisProgress.value = 0
    _analysisStatusText.value = "Clip ready for analysis (${clip.duration})"
    showToast("Attached ${clip.name}", "movie")
  }

  fun selectClipFromUri(context: Context, uri: Uri) {
    viewModelScope.launch {
      val info = VideoMetadataHelper.extractVideoInfo(context, uri)
      val userClip = ReferenceClip(
        id = "user_${System.currentTimeMillis()}",
        name = info.displayName,
        duration = info.durationFormatted,
        durationMs = info.durationMs,
        resolution = "${info.width}x${info.height} ${info.frameRate}",
        motionStreamsCount = 1,
        uriString = uri.toString(),
        mimeType = info.mimeType,
        sizeBytes = info.sizeBytes,
        isSampleClip = false,
        videoBase64 = info.videoBase64,
        keyframeBase64List = info.keyframeBase64List,
        timestampedKeyframes = info.timestampedKeyframes,
        thumbnailTheme = "user"
      )
      selectClip(userClip)
      showToast("Imported ${info.displayName} (${info.durationFormatted})", "video_file")
    }
  }

  fun clearSelectedClip() {
    _selectedClip.value = null
    _analysisResult.value = null
    _editableBasePrompt.value = ""
    _analysisErrorMessage.value = null
    _analysisProgress.value = 0
    _analysisStatusText.value = "Awaiting video upload"
  }

  fun updateEditableBasePrompt(prompt: String) {
    _editableBasePrompt.value = prompt
  }

  fun resetBasePromptToAnalyzed() {
    val analyzed = _analysisResult.value?.extractedBasePrompt ?: return
    _editableBasePrompt.value = analyzed
    showToast("Reset base prompt to analyzed blueprint", "refresh")
  }

  fun startVideoAnalysis() {
    val clip = _selectedClip.value
    if (clip == null) {
      showToast("Please select a video first", "warning")
      return
    }

    _isAnalyzing.value = true
    _analysisProgress.value = 0
    _analysisErrorMessage.value = null

    viewModelScope.launch {
      val result = geminiService.analyzeVideo(clip) { progress, status ->
        _analysisProgress.value = progress
        _analysisStatusText.value = status
      }

      when (result) {
        is ServiceResult.Success -> {
          _analysisResult.value = result.data
          _editableBasePrompt.value = result.data.extractedBasePrompt
          _analysisErrorMessage.value = null
          _isAnalyzing.value = false
          showToast("${result.data.modelUsed} analysis complete!", "psychology")
        }
        is ServiceResult.Error -> {
          _analysisResult.value = null
          _analysisErrorMessage.value = result.message
          _isAnalyzing.value = false
          showToast("Analysis failed: See details", "error")
        }
      }
    }
  }

  fun proceedToCombineIdea() {
    if (_analysisResult.value == null && _editableBasePrompt.value.isBlank()) {
      startVideoAnalysis()
      return
    }
    val clip = _selectedClip.value
    if (clip != null) {
      _combinedPromptBlueprint.value = geminiService.generateCombinedPromptBlueprint(
        clip = clip,
        basePrompt = _editableBasePrompt.value,
        idea = _userIdea.value,
        params = _diffusionParams.value,
        aspectRatio = _selectedAspectRatio.value
      )
    }
    _createStep.value = CreateStep.COMBINE_IDEA
  }

  fun updateUserIdea(idea: String) {
    _userIdea.value = idea
  }

  fun updateCombinedPromptBlueprint(blueprint: String) {
    _combinedPromptBlueprint.value = blueprint
  }

  fun triggerAiEnhance() {
    _isEnhancingIdea.value = true
    viewModelScope.launch {
      val basePrompt = _editableBasePrompt.value.ifBlank {
        _analysisResult.value?.extractedBasePrompt ?: ""
      }
      val result = geminiService.enhanceCreativeIdea(
        _userIdea.value,
        basePrompt,
        _diffusionParams.value.selectedPreset
      )

      when (result) {
        is ServiceResult.Success -> {
          _userIdea.value = result.data
          val clip = _selectedClip.value
          if (clip != null) {
            _combinedPromptBlueprint.value = geminiService.generateCombinedPromptBlueprint(
              clip = clip,
              basePrompt = basePrompt,
              idea = result.data,
              params = _diffusionParams.value,
              aspectRatio = _selectedAspectRatio.value
            )
          }
          showToast("Creative idea enhanced with Gemini!", "auto_awesome")
        }
        is ServiceResult.Error -> {
          showToast(result.message, "error")
        }
      }
      _isEnhancingIdea.value = false
    }
  }

  fun updateDiffusionMotion(motion: Int) {
    _diffusionParams.value = _diffusionParams.value.copy(motionFidelity = motion)
  }

  fun updateDiffusionStyle(style: Int) {
    _diffusionParams.value = _diffusionParams.value.copy(styleOverwrite = style)
  }

  fun selectPreset(presetName: String) {
    _diffusionParams.value = _diffusionParams.value.copy(selectedPreset = presetName)
    showToast("Preset applied: $presetName", "palette")
  }

  fun togglePreserveGeometry() {
    _diffusionParams.value = _diffusionParams.value.copy(
      preserveBackgroundGeometry = !_diffusionParams.value.preserveBackgroundGeometry
    )
  }

  fun toggleAiSoundEffects() {
    _diffusionParams.value = _diffusionParams.value.copy(
      generateAiSoundEffects = !_diffusionParams.value.generateAiSoundEffects
    )
  }

  fun generatePromptBlueprint() {
    val clip = _selectedClip.value
    if (clip == null) {
      showToast("Please select a reference clip first", "warning")
      return
    }

    val basePrompt = _editableBasePrompt.value.ifBlank {
      _analysisResult.value?.extractedBasePrompt ?: ""
    }
    _combinedPromptBlueprint.value = geminiService.generateCombinedPromptBlueprint(
      clip = clip,
      basePrompt = basePrompt,
      idea = _userIdea.value,
      params = _diffusionParams.value,
      aspectRatio = _selectedAspectRatio.value
    )
    showToast("Combined prompt blueprint synthesized!", "auto_awesome")
  }

  fun startVideoGeneration(outputDir: File? = null, onCompleted: () -> Unit = {}) {
    val clip = _selectedClip.value
    if (clip == null) {
      showToast("No reference video selected", "warning")
      return
    }

    if (!repository.deductCredits(10)) {
      showToast("Insufficient credits! Please top up.", "error")
      _showCreditDialog.value = true
      return
    }

    _isGenerating.value = true
    _generationProgress.value = 0
    _generationStatusText.value = "Initializing synthesis..."
    _generationErrorMessage.value = null

    val basePrompt = _editableBasePrompt.value.ifBlank {
      _analysisResult.value?.extractedBasePrompt ?: ""
    }
    val finalPrompt = if (
      _combinedPromptBlueprint.value.isBlank() ||
      _combinedPromptBlueprint.value.contains("Your combined prompt")
    ) {
      geminiService.generateCombinedPromptBlueprint(
        clip = clip,
        basePrompt = basePrompt,
        idea = _userIdea.value,
        params = _diffusionParams.value,
        aspectRatio = _selectedAspectRatio.value
      )
    } else {
      _combinedPromptBlueprint.value
    }

    val projectId = (89100 + (1..899).random()).toString()
    val newProject = VideoProject(
      id = projectId,
      title = "Video Generation #$projectId",
      subtitle = "Gemini Omni 1.1 Flash • ${_selectedAspectRatio.value.label}",
      timestamp = "Just now",
      status = ProjectStatus.PROCESSING,
      creditsUsed = 10,
      referenceClip = clip,
      extractedBasePrompt = basePrompt,
      userIdea = _userIdea.value.ifBlank { _diffusionParams.value.selectedPreset },
      synthesizedPrompt = finalPrompt,
      diffusionParameters = _diffusionParams.value,
      aspectRatio = _selectedAspectRatio.value,
      analysisModel = _analysisResult.value?.modelUsed ?: geminiService.analysisModelName,
      generationModel = geminiService.generationModelName
    )

    _activeProject.value = newProject

    viewModelScope.launch {
      val result = geminiService.generateVideo(
        project = newProject,
        aspectRatio = _selectedAspectRatio.value,
        outputDir = outputDir
      ) { prog, status ->
        _generationProgress.value = prog
        _generationStatusText.value = status
      }

      when (result) {
        is ServiceResult.Success -> {
          val completed = newProject.copy(
            status = ProjectStatus.COMPLETED,
            seeds = result.data.seeds,
            aspectRatio = result.data.aspectRatio,
            fileSizeMb = result.data.fileSizeMb,
            videoOutputUri = result.data.outputVideoUri,
            operationId = result.data.operationId,
            generationModeLabel = result.data.generationModeLabel
          )
          _activeProject.value = completed
          repository.saveProject(completed)
          _isGenerating.value = false
          _createStep.value = CreateStep.GENERATE
          showToast("Video rendered with ${result.data.modelUsed}!", "play_circle")
          onCompleted()
        }
        is ServiceResult.Error -> {
          // Refund credits on failure so user is not charged for failed API call
          repository.refundCredits(10)
          val failedProject = newProject.copy(
            status = ProjectStatus.FAILED,
            errorMessage = result.message,
            videoOutputUri = null
          )
          _activeProject.value = failedProject
          _generationErrorMessage.value = result.message
          _isGenerating.value = false
          showToast("Generation error: See details", "error")
        }
      }
    }
  }

  fun openProject(project: VideoProject) {
    _activeProject.value = project
    _currentTab.value = AppTab.CREATE
    _createStep.value = CreateStep.GENERATE
  }

  fun setActiveSeed(index: Int) {
    val project = _activeProject.value ?: return
    _activeProject.value = project.copy(activeSeedIndex = index)
    repository.updateActiveSeed(project.id, index)
    showToast("Switched to ${project.seeds.getOrNull(index)?.label ?: "seed"}", "shuffle")
  }

  fun toggleFavorite(id: String) {
    repository.toggleFavorite(id)
    val active = _activeProject.value
    if (active != null && active.id == id) {
      _activeProject.value = active.copy(isFavorite = !active.isFavorite)
    }
    showToast("Updated library bookmark", "bookmark")
  }

  fun deleteProject(id: String) {
    repository.deleteProject(id)
    if (_activeProject.value?.id == id) {
      _activeProject.value = null
      _createStep.value = CreateStep.UPLOAD_ANALYZE
    }
    showToast("Project removed", "delete")
  }

  fun setAspectRatio(ratio: VideoAspectRatio) {
    _selectedAspectRatio.value = ratio
    showToast("Aspect ratio set to ${ratio.label}", "aspect_ratio")
  }

  fun setHistoryTab(tab: HistoryFilterTab) {
    _historyTab.value = tab
  }

  fun setStatusFilter(filter: ProjectStatusFilter) {
    _statusFilter.value = filter
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun toggleSelectMode() {
    _isSelectMode.value = !_isSelectMode.value
    if (!_isSelectMode.value) {
      _selectedProjectIds.value = emptySet()
    }
  }

  fun toggleProjectSelection(id: String) {
    val current = _selectedProjectIds.value.toMutableSet()
    if (current.contains(id)) current.remove(id) else current.add(id)
    _selectedProjectIds.value = current
  }

  fun deleteSelectedProjects() {
    val ids = _selectedProjectIds.value
    ids.forEach { repository.deleteProject(it) }
    _selectedProjectIds.value = emptySet()
    _isSelectMode.value = false
    showToast("Deleted ${ids.size} projects", "delete")
  }

  fun addCredits(amount: Int) {
    repository.addCredits(amount)
    showToast("Added $amount credits to balance!", "bolt")
  }

  fun showToast(message: String, icon: String = "check_circle") {
    _toastEvent.value = ToastEvent(message, icon)
  }

  fun dismissToast() {
    _toastEvent.value = null
  }

  fun setCreditDialogVisible(visible: Boolean) {
    _showCreditDialog.value = visible
  }

  fun setNotificationDialogVisible(visible: Boolean) {
    _showNotificationDialog.value = visible
  }

  fun setProfileDialogVisible(visible: Boolean) {
    _showProfileDialog.value = visible
  }
}
