package com.fernandohbrasil.openlibraryandroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun gradientBrush(): Brush {
    val dark = isSystemInDarkTheme()
    // Preview-safe fallback
    val isPreview = LocalInspectionMode.current
    return if (dark && !isPreview) {
        Brush.linearGradient(
            colors = listOf(
                Grey900, // top-left
                Grey800,
                Grey700, // center
                Grey800,
                Grey900  // bottom-right
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Grey100, // top-left
                Grey200,
                Grey400, // center
                Grey200,
                Grey100  // bottom-right
            )
        )
    }
}
