package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkGameColors.primary,
    onPrimary = DarkGameColors.onPrimary,
    primaryContainer = DarkGameColors.primaryContainer,
    onPrimaryContainer = DarkGameColors.onPrimaryContainer,
    secondary = PurpleGrey80,
    secondaryContainer = DarkGameColors.secondaryContainer,
    onSecondaryContainer = DarkGameColors.onSecondaryContainer,
    tertiary = Pink80,
    background = DarkGameColors.bg,
    onBackground = DarkGameColors.textPrimary,
    surface = DarkGameColors.surface,
    onSurface = DarkGameColors.textPrimary,
    surfaceVariant = DarkGameColors.surfaceVariant,
    onSurfaceVariant = DarkGameColors.textSecondary,
    outline = DarkGameColors.outline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = LightGameColors.primary,
    onPrimary = LightGameColors.onPrimary,
    primaryContainer = LightGameColors.primaryContainer,
    onPrimaryContainer = LightGameColors.onPrimaryContainer,
    secondary = PurpleGrey40,
    secondaryContainer = LightGameColors.secondaryContainer,
    onSecondaryContainer = LightGameColors.onSecondaryContainer,
    tertiary = Pink40,
    background = LightGameColors.bg,
    onBackground = LightGameColors.textPrimary,
    surface = LightGameColors.surface,
    onSurface = LightGameColors.textPrimary,
    surfaceVariant = LightGameColors.surfaceVariant,
    onSurfaceVariant = LightGameColors.textSecondary,
    outline = LightGameColors.outline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val gameColors = if (darkTheme) DarkGameColors else LightGameColors
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  CompositionLocalProvider(LocalGameColors provides gameColors) {
    MaterialExpressiveTheme(
      colorScheme = colorScheme,
      motionScheme = MotionScheme.expressive(),
      typography = Typography,
      shapes = ExpressiveShapes,
      content = content
    )
  }
}
