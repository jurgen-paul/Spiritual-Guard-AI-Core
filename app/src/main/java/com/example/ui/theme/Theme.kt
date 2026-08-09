package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val SpiritualDarkColorScheme = darkColorScheme(
    primary = SpiritualGold,
    onPrimary = CosmicIndigo,
    primaryContainer = CosmicPurple,
    onPrimaryContainer = SoftGold,
    secondary = SereneTeal,
    onSecondary = CosmicIndigo,
    secondaryContainer = CosmicNavy,
    onSecondaryContainer = SereneCyan,
    tertiary = SereneCyan,
    onTertiary = CosmicIndigo,
    background = CosmicIndigo,
    onBackground = CelestialWhite,
    surface = CardDark,
    onSurface = CelestialWhite,
    surfaceVariant = CosmicNavy,
    onSurfaceVariant = CelestialWhite
)

private val SpiritualLightColorScheme = lightColorScheme(
    primary = SpiritualGoldVariant,
    onPrimary = Color.White,
    primaryContainer = SoftGold,
    onPrimaryContainer = CosmicIndigo,
    secondary = SereneTeal,
    onSecondary = Color.White,
    tertiary = SereneCyan,
    background = Color(0xFFF8FAFC),
    onBackground = CosmicIndigo,
    surface = Color.White,
    onSurface = CosmicIndigo
)

@Composable
fun SpiritualGuardTheme(
    darkTheme: Boolean = true, // Default to rich dark serene cosmic aesthetic
    dynamicColor: Boolean = false, // Preserve brand spiritual colors
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SpiritualDarkColorScheme else SpiritualLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    SpiritualGuardTheme(darkTheme = darkTheme, content = content)
}

