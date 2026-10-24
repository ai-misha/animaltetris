package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6750A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)

// Backward compatibility references
val SleekBg = Color(0xFF141218)
val SleekTextPrimary = Color(0xFFE6E0E9)
val SleekTextSecondary = Color(0xFFCAC4D0)
val SleekTextTertiary = Color(0xFF938F99)

val SleekPrimary = Color(0xFFD0BCFF)
val SleekOnPrimary = Color(0xFF381E72)
val SleekPrimaryContainer = Color(0xFF4F378B)
val SleekOnPrimaryContainer = Color(0xFFEADDFF)

val SleekSecondaryContainer = Color(0xFF332D41)
val SleekOnSecondaryContainer = Color(0xFFE8DEF8)

val SleekSurface = Color(0xFF1D1A22)
val SleekSurfaceContainer = Color(0xFF18151F)
val SleekSurfaceVariant = Color(0xFF2B2636)
val SleekOutline = Color(0xFF49454F)

val SleekAccentRotate = Color(0xFF4F378B)
val SleekAccentRotateDark = Color(0xFFEADDFF)
val SleekAccentDrop = Color(0xFFD0BCFF)

@Immutable
data class GameColorScheme(
    val isDark: Boolean,
    val bg: Color,
    val surface: Color,
    val surfaceContainer: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val dropBtnBg: Color,
    val dropBtnContent: Color,
    val rotateBtnBg: Color,
    val rotateBtnContent: Color,
    val circleBtnBg: Color,
    val circleBtnContent: Color,
    val previewBg: Color,
    val previewBorder: Color,
    val gridColor: Color,
    val dialogBg: Color,
    val iconTint: Color,
    val error: Color
)

val DarkGameColors = GameColorScheme(
    isDark = true,
    bg = Color(0xFF141218),
    surface = Color(0xFF1E1A24),
    surfaceContainer = Color(0xFF18151F),
    surfaceVariant = Color(0xFF2B2636),
    outline = Color(0xFF49454F),
    textPrimary = Color(0xFFE6E0E9),
    textSecondary = Color(0xFFCAC4D0),
    textTertiary = Color(0xFF938F99),
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondaryContainer = Color(0xFF332D41),
    onSecondaryContainer = Color(0xFFE8DEF8),
    dropBtnBg = Color(0xFFD0BCFF),
    dropBtnContent = Color(0xFF21005D),
    rotateBtnBg = Color(0xFF4F378B),
    rotateBtnContent = Color(0xFFEADDFF),
    circleBtnBg = Color(0xFF332D41),
    circleBtnContent = Color(0xFFD0BCFF),
    previewBg = Color(0xFF1E1A24),
    previewBorder = Color(0xFF3B3547),
    gridColor = Color(0xFF363042).copy(alpha = 0.55f),
    dialogBg = Color(0xFF201C27),
    iconTint = Color(0xFFD0BCFF),
    error = Color(0xFFF2B8B5)
)

val LightGameColors = GameColorScheme(
    isDark = false,
    bg = Color(0xFFFEF7FF),
    surface = Color.White,
    surfaceContainer = Color(0xFFF3EDF7),
    surfaceVariant = Color(0xFFE7E0EC),
    outline = Color(0xFFCAC4D0),
    textPrimary = Color(0xFF1D1B20),
    textSecondary = Color(0xFF49454F),
    textTertiary = Color(0xFF79747E),
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    dropBtnBg = Color(0xFF6750A4),
    dropBtnContent = Color.White,
    rotateBtnBg = Color(0xFFD0BCFF),
    rotateBtnContent = Color(0xFF21005D),
    circleBtnBg = Color(0xFFEADDFF),
    circleBtnContent = Color(0xFF21005D),
    previewBg = Color.White,
    previewBorder = Color(0xFFCAC4D0).copy(alpha = 0.6f),
    gridColor = Color(0xFFCAC4D0).copy(alpha = 0.5f),
    dialogBg = Color(0xFFFEF7FF),
    iconTint = Color(0xFF6750A4),
    error = Color(0xFFB3261E)
)

val LocalGameColors = staticCompositionLocalOf { DarkGameColors }
