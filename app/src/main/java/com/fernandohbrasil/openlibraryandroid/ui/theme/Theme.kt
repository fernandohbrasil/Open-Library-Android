package com.fernandohbrasil.openlibraryandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey100
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey200
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey300
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey400
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey500
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey700
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey800
import com.fernandohbrasil.openlibraryandroid.ui.theme.Grey900

private val DarkColorScheme = darkColorScheme(
    primary = Grey300,
    onPrimary = Grey900,
    secondary = Grey500,
    onSecondary = Grey900,
    tertiary = Grey700,
    background = Grey900,
    onBackground = Grey100,
    surface = Grey800,
    onSurface = Grey100,
)

private val LightColorScheme = lightColorScheme(
    primary = Grey900,
    onPrimary = Grey100,
    secondary = Grey600,
    onSecondary = Grey100,
    tertiary = Grey300,
    background = Grey100,
    onBackground = Grey900,
    surface = Grey200,
    onSurface = Grey900,
)

@Composable
fun OpenLibraryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            // Light status bar icons in light theme, dark icons in dark theme would be unreadable.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
