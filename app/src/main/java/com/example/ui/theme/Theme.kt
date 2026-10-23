package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FeeTrackLightColorScheme = lightColorScheme(
  primary = PrimaryDeepTeal,
  onPrimary = Color.White,
  primaryContainer = PrimaryTealContainer,
  onPrimaryContainer = OnPrimaryTealContainer,
  secondary = PrimaryTealLight,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0F2FE),
  onSecondaryContainer = Color(0xFF0369A1),
  tertiary = PendingAmber,
  onTertiary = Color.White,
  tertiaryContainer = PendingAmberContainer,
  onTertiaryContainer = OnPendingAmberContainer,
  background = BackgroundSlate,
  onBackground = TextPrimary,
  surface = SurfaceWhite,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceVariantSlate,
  onSurfaceVariant = TextSecondary,
  outline = SurfaceBorder,
  error = WarningRed,
  onError = Color.White,
  errorContainer = WarningRedContainer,
  onErrorContainer = OnWarningRedContainer,
)

private val FeeTrackDarkColorScheme = darkColorScheme(
  primary = PrimaryTealLight,
  onPrimary = Color(0xFF003733),
  primaryContainer = PrimaryDarkTeal,
  onPrimaryContainer = PrimaryTealContainer,
  secondary = PrimaryTealLight,
  onSecondary = Color.White,
  background = Color(0xFF0F172A),
  onBackground = Color(0xFFF8FAFC),
  surface = Color(0xFF1E293B),
  onSurface = Color(0xFFF8FAFC),
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF475569),
  error = WarningRed,
  onError = Color.White,
)

@Composable
fun FeeTrackTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Prefer FeeTrack explicit deep teal branding by default
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) FeeTrackDarkColorScheme else FeeTrackLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
