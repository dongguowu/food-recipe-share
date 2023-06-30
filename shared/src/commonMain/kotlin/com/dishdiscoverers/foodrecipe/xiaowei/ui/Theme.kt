package com.dishdiscoverers.foodrecipe.xiaowei.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val LightColors = lightColorScheme(
    primary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_primary,
    onPrimary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onPrimary,
    primaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_primaryContainer,
    onPrimaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onPrimaryContainer,
    secondary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_secondary,
    onSecondary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onSecondary,
    secondaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_secondaryContainer,
    onSecondaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onSecondaryContainer,
    tertiary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_tertiary,
    onTertiary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onTertiary,
    tertiaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_tertiaryContainer,
    onTertiaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onTertiaryContainer,
    error = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_error,
    errorContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_errorContainer,
    onError = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onError,
    onErrorContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onErrorContainer,
    background = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_background,
    onBackground = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onBackground,
    surface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_surface,
    onSurface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onSurface,
    surfaceVariant = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_surfaceVariant,
    onSurfaceVariant = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_onSurfaceVariant,
    outline = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_outline,
    inverseOnSurface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_inverseOnSurface,
    inverseSurface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_inverseSurface,
    inversePrimary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_inversePrimary,
    surfaceTint = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_surfaceTint,
    outlineVariant = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_outlineVariant,
    scrim = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_primary,
    onPrimary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onPrimary,
    primaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_primaryContainer,
    onPrimaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onPrimaryContainer,
    secondary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_secondary,
    onSecondary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onSecondary,
    secondaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_secondaryContainer,
    onSecondaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onSecondaryContainer,
    tertiary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_tertiary,
    onTertiary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onTertiary,
    tertiaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_tertiaryContainer,
    onTertiaryContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onTertiaryContainer,
    error = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_error,
    errorContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_errorContainer,
    onError = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onError,
    onErrorContainer = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onErrorContainer,
    background = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_background,
    onBackground = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onBackground,
    surface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_surface,
    onSurface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onSurface,
    surfaceVariant = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_surfaceVariant,
    onSurfaceVariant = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_onSurfaceVariant,
    outline = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_outline,
    inverseOnSurface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_inverseOnSurface,
    inverseSurface = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_inverseSurface,
    inversePrimary = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_inversePrimary,
    surfaceTint = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_surfaceTint,
    outlineVariant = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_outlineVariant,
    scrim = com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_scrim,
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkColors
  }

  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}