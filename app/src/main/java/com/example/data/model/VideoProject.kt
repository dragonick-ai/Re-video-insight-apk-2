package com.example.data.model

enum class ProjectStatus {
  COMPLETED,
  PROCESSING,
  DRAFT,
  FAILED
}

enum class VideoAspectRatio(val label: String, val ratioFloat: Float) {
  LANDSCAPE("16:9", 16f / 9f),
  PORTRAIT("9:16", 9f / 16f)
}

data class KeyframeSample(
  val timestamp: String,
  val base64: String
)

data class TimestampEvent(
  val timestamp: String,
  val description: String
)

data class CapabilityValidationReport(
  val analysisModelId: String = "gemini-4-argon",
  val fallbackAnalysisModelId: String = "gemini-3.8-flash",
  val generationModelId: String = "gemini-omni-1.1-flash",
  val supportedInputMimeTypes: List<String> = listOf("video/mp4", "video/quicktime", "video/webm", "image/jpeg"),
  val maxInlineVideoSizeMb: Int = 15,
  val maxRecommendedDurationSec: Int = 60,
  val supportedOutputFormats: List<String> = listOf("video/mp4 (16:9)", "video/mp4 (9:16)"),
  val isApiKeyConfigured: Boolean = false,
  val supportsReferenceVideoInput: Boolean = true,
  val transformationLimitationNote: String = "Direct video-to-video synthesis via gemini-omni-1.1-flash requires Interactions API access with multimodal reference video input enabled on the configured API key or a backend Vertex AI proxy. Text-only or frame-only generation is never substituted silently for reference-video transformation."
)

data class ReferenceClip(
  val id: String,
  val name: String,
  val duration: String = "0:15",
  val durationMs: Long = 15000L,
  val resolution: String = "1080p 60fps",
  val motionStreamsCount: Int = 1,
  val uriString: String? = null,
  val mimeType: String = "video/mp4",
  val sizeBytes: Long = 0L,
  val isSampleClip: Boolean = false,
  val videoBase64: String? = null,
  val keyframeBase64List: List<String> = emptyList(),
  val timestampedKeyframes: List<KeyframeSample> = emptyList(),
  val thumbnailTheme: String = "parkour"
)

data class SceneLayer(
  val id: String,
  val title: String,
  val description: String,
  val type: String,
  val timestampRange: String? = null
)

data class DiffusionParameters(
  val motionFidelity: Int = 80,
  val styleOverwrite: Int = 85,
  val selectedPreset: String = "Cyberpunk Neo-Tokyo",
  val preserveBackgroundGeometry: Boolean = true,
  val generateAiSoundEffects: Boolean = true
)

data class GenerationSeed(
  val id: String,
  val label: String,
  val code: String,
  val name: String,
  val motionPercent: Int = 85,
  val stylePercent: Int = 90
)

data class VideoProject(
  val id: String,
  val title: String,
  val subtitle: String = "Gemini Omni 1.1 Flash • 1080p",
  val timestamp: String = "Just now",
  val createdAt: Long = System.currentTimeMillis(),
  val status: ProjectStatus = ProjectStatus.COMPLETED,
  val creditsUsed: Int = 10,
  val referenceClip: ReferenceClip,
  val extractedBasePrompt: String = "",
  val userIdea: String,
  val synthesizedPrompt: String,
  val diffusionParameters: DiffusionParameters = DiffusionParameters(),
  val seeds: List<GenerationSeed> = listOf(
    GenerationSeed("s1", "Seed #1", "#89104-A", "Active Render", 85, 90),
    GenerationSeed("s2", "Seed #2", "#89104-B", "Variant 02", 82, 94),
    GenerationSeed("s3", "Seed #3", "#89104-C", "Variant 03", 88, 79)
  ),
  val activeSeedIndex: Int = 0,
  val isFavorite: Boolean = false,
  val fileSizeMb: Double = 0.0,
  val aspectRatio: VideoAspectRatio = VideoAspectRatio.LANDSCAPE,
  val analysisModel: String = "gemini-4-argon",
  val generationModel: String = "gemini-omni-1.1-flash",
  val generationModeLabel: String = "Video-to-Video (Reference + Prompt)",
  val videoOutputUri: String? = null,
  val operationId: String? = null,
  val errorMessage: String? = null
)
