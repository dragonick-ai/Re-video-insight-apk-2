package com.example.data.model

data class TransferPreset(
  val id: String,
  val name: String,
  val iconName: String,
  val description: String,
  val defaultPromptSnippet: String,
  val motionFidelity: Int = 80,
  val styleOverwrite: Int = 85
)

object PresetCatalog {
  val allPresets = listOf(
    TransferPreset(
      id = "style_transfer",
      name = "Style Transfer",
      iconName = "brush",
      description = "Neural texture transfer preserving underlying kinetic flow.",
      defaultPromptSnippet = "Fluid stylized color grade, dynamic cinematic lighting, volumetric mist.",
      motionFidelity = 85,
      styleOverwrite = 80
    ),
    TransferPreset(
      id = "motion_retargeting",
      name = "Motion Retargeting",
      iconName = "directions_run",
      description = "Maps movement trajectories onto completely transformed character models.",
      defaultPromptSnippet = "Precise human kinematics mapped onto advanced mechanical cybernetic suit.",
      motionFidelity = 95,
      styleOverwrite = 75
    ),
    TransferPreset(
      id = "cinematic_anime",
      name = "Cinematic Anime",
      iconName = "animation",
      description = "Modern anime aesthetic with hand-painted background layers and dynamic speedlines.",
      defaultPromptSnippet = "Makoto Shinkai aesthetic, lush cloudscapes, vibrant cerulean skies, hand-drawn keyframes.",
      motionFidelity = 80,
      styleOverwrite = 90
    ),
    TransferPreset(
      id = "cyberpunk",
      name = "Cyberpunk",
      iconName = "electric_bolt",
      description = "Neo-Tokyo nightscapes, drenched pavement reflections, and holographic arrays.",
      defaultPromptSnippet = "Chrome cyborg ninja, blazing cyan visor, neon-reflecting wet rooftops, volumetric smoke, raytraced blue lightning.",
      motionFidelity = 80,
      styleOverwrite = 85
    ),
    TransferPreset(
      id = "claymation",
      name = "Claymation",
      iconName = "draw",
      description = "Tactile stop-motion clay texture with simulated 12fps shutter cadence.",
      defaultPromptSnippet = "Tactile stop-motion plasticine model, visible fingerprint textures, studio macro lighting.",
      motionFidelity = 70,
      styleOverwrite = 95
    ),
    TransferPreset(
      id = "dark_fantasy",
      name = "Dark Fantasy",
      iconName = "fort",
      description = "Gothic architecture, atmospheric torches, dark ethereal magic.",
      defaultPromptSnippet = "Eldritch gothic knight in ornate blackened steel plate, crumbling stone spires, crimson embers.",
      motionFidelity = 85,
      styleOverwrite = 85
    ),
    TransferPreset(
      id = "photoreal_8k",
      name = "Photorealistic 8K",
      iconName = "camera",
      description = "Ultra high-definition IMAX 70mm lens reproduction with authentic depth of field.",
      defaultPromptSnippet = "IMAX 70mm shot, master color grading, natural sun flare, razor sharp sub-surface scattering.",
      motionFidelity = 90,
      styleOverwrite = 70
    )
  )
}
