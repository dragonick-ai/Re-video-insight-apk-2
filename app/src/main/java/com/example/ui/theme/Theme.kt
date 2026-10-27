package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ReVideoColorScheme = darkColorScheme(
  primary = ReVideoPrimary,
  onPrimary = Color.White,
  primaryContainer = ReVideoPrimaryContainer,
  onPrimaryContainer = Color.White,
  inversePrimary = ReVideoPrimaryDark,
  secondary = ReVideoCyan,
  onSecondary = Color(0xFF003640),
  secondaryContainer = ReVideoCyanDim,
  onSecondaryContainer = Color(0xFF001F26),
  tertiary = ReVideoEmerald,
  onTertiary = Color(0xFF003824),
  tertiaryContainer = ReVideoEmeraldContainer,
  onTertiaryContainer = Color.White,
  background = ReVideoCanvas,
  onBackground = ReVideoTextHigh,
  surface = ReVideoCanvas,
  onSurface = ReVideoTextHigh,
  surfaceVariant = ReVideoSurface1,
  onSurfaceVariant = ReVideoTextMedium,
  surfaceTint = ReVideoPrimaryLight,
  inverseSurface = ReVideoTextHigh,
  inverseOnSurface = ReVideoSurface2,
  outline = ReVideoOutline,
  outlineVariant = ReVideoBorder,
  error = ReVideoError,
  onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // ReVideo is a dark creative studio
  dynamicColor: Boolean = false, // Preserve Stitch design tokens strictly
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = ReVideoColorScheme,
    typography = Typography,
    content = content
  )
}
