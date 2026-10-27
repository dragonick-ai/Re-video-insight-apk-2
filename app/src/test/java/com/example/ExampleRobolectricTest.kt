package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.KeyframeSample
import com.example.data.model.ReferenceClip
import com.example.data.model.VideoAspectRatio
import com.example.data.repository.GeminiVideoService
import com.example.data.repository.ProjectRepository
import com.example.viewmodel.ReVideoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ReVideo AI", appName)
  }

  @Test
  fun `verify initial project history is strictly empty without fake data`() {
    val repo = ProjectRepository()
    assertEquals(0, repo.projects.value.size)
    assertEquals(45, repo.credits.value)
  }

  @Test
  fun `verify credit deduction and refund`() {
    val repo = ProjectRepository()
    val deducted = repo.deductCredits(10)
    assertEquals(true, deducted)
    assertEquals(35, repo.credits.value)

    repo.refundCredits(10)
    assertEquals(45, repo.credits.value)
  }

  @Test
  fun `verify capability validation report and model identifiers`() {
    val service = GeminiVideoService()
    val report = service.getCapabilityValidationReport()
    assertEquals("gemini-4-argon", report.analysisModelId)
    assertEquals("gemini-3.8-flash", report.fallbackAnalysisModelId)
    assertEquals("gemini-omni-1.1-flash", report.generationModelId)
    assertTrue(report.supportedInputMimeTypes.contains("video/mp4"))
    assertTrue(report.supportedOutputFormats.any { it.contains("16:9") })
    assertTrue(report.supportedOutputFormats.any { it.contains("9:16") })
  }

  @Test
  fun `verify video analysis json parsing extracts scene actions camera style lighting and timestamps`() {
    val service = GeminiVideoService()
    val sampleJson = """
      {
        "scene_description": "Rain-slicked rooftop skyline at dusk with industrial vents.",
        "actions": "Athlete vaults over concrete barrier and lands in a roll.",
        "camera_movement": "Low-angle tracking dolly moving forward at high velocity.",
        "visual_style": "High-contrast cinematic teal and orange grade.",
        "lighting": "Volumetric rim lighting from neon signage through mist.",
        "timestamps": [
          {"timestamp": "0:00-0:04", "description": "Approach sprint along rooftop edge"},
          {"timestamp": "0:04-0:09", "description": "Mid-air vault over gap"}
        ],
        "extracted_prompt": "Low-angle tracking shot of an athlete vaulting across a rain-slicked rooftop at dusk, volumetric rim lighting."
      }
    """.trimIndent()

    val result = service.parseAnalysisJson(sampleJson, "gemini-4-argon", "0:09")
    assertEquals("gemini-4-argon", result.modelUsed)
    assertEquals("Rain-slicked rooftop skyline at dusk with industrial vents.", result.sceneDescription)
    assertEquals("Athlete vaults over concrete barrier and lands in a roll.", result.actions)
    assertEquals("Low-angle tracking dolly moving forward at high velocity.", result.cameraMovement)
    assertEquals("High-contrast cinematic teal and orange grade.", result.visualStyle)
    assertEquals("Volumetric rim lighting from neon signage through mist.", result.lighting)
    assertEquals(2, result.timestamps.size)
    assertEquals("0:00-0:04", result.timestamps[0].timestamp)
    assertEquals(6, result.layers.size)
    assertTrue(result.extractedBasePrompt.contains("Low-angle tracking shot"))
  }

  @Test
  fun `verify editable base prompt and user idea combination workflow`() {
    val viewModel = ReVideoViewModel()
    val clip = ReferenceClip(
      id = "test_clip",
      name = "Rooftop_Run.mp4",
      duration = "0:10",
      durationMs = 10000L,
      resolution = "1920x1080 60fps",
      mimeType = "video/mp4",
      timestampedKeyframes = listOf(KeyframeSample("0:02", "dGVzdA=="))
    )

    viewModel.selectClip(clip)
    assertEquals("Rooftop_Run.mp4", viewModel.selectedClip.value?.name)

    // User edits the extracted base prompt
    viewModel.updateEditableBasePrompt("Custom edited base prompt: dynamic rooftop tracking shot")
    assertEquals("Custom edited base prompt: dynamic rooftop tracking shot", viewModel.editableBasePrompt.value)

    // User enters creative idea and selects aspect ratio
    viewModel.updateUserIdea("Transform into cyberpunk anime with neon lightning")
    viewModel.setAspectRatio(VideoAspectRatio.PORTRAIT)
    viewModel.generatePromptBlueprint()

    val combined = viewModel.combinedPromptBlueprint.value
    assertTrue(combined.contains("Custom edited base prompt: dynamic rooftop tracking shot"))
    assertTrue(combined.contains("Transform into cyberpunk anime with neon lightning"))
    assertTrue(combined.contains("9:16"))

    // User further edits the combined prompt
    viewModel.updateCombinedPromptBlueprint("$combined [User Final Tweak]")
    assertTrue(viewModel.combinedPromptBlueprint.value.endsWith("[User Final Tweak]"))
  }

  @Test
  fun `verify clip duration validation enforces limits`() {
    val service = GeminiVideoService()
    val validClip = ReferenceClip(id = "c1", name = "short.mp4", duration = "0:30", durationMs = 30_000L)
    val tooLongClip = ReferenceClip(id = "c2", name = "long.mp4", duration = "3:00", durationMs = 180_000L)

    assertNull(service.validateClipCapabilities(validClip))
    assertNotNull(service.validateClipCapabilities(tooLongClip))
  }
}
