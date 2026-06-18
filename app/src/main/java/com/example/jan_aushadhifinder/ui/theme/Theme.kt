package com.example.jan_aushadhifinder.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = HealthcareBlue,
    secondary = EmeraldGreen,
    tertiary = GenericSectionBlue,
    background = Color(0xFF111827), // Darker slate for premium dark mode
    surface = Color(0xFF1F2937),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF374151)
)

private val LightColorScheme = lightColorScheme(
    primary = HealthcareBlue,
    secondary = HealthcareBlueDark,
    tertiary = EmeraldGreen,
    background = OffWhite,
    surface = ClinicalWhite,
    onPrimary = ClinicalWhite,
    onSecondary = ClinicalWhite,
    onTertiary = ClinicalWhite,
    onBackground = DarkSlate,
    onSurface = DarkSlate,
    surfaceVariant = ClinicalWhite,
    onSurfaceVariant = SoftGrey,
    primaryContainer = GenericSectionBlue,
    onPrimaryContainer = HealthcareBlue,
    error = StrikethroughRed
)

@Composable
fun JanAushadhiFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for strict brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
