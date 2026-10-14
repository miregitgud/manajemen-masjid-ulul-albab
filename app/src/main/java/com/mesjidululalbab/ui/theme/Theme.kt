package com.mesjidululalbab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MasjidColorScheme = darkColorScheme(
    primary = Emas,
    onPrimary = Hijau,
    primaryContainer = HijauKartu,
    onPrimaryContainer = Krem,
    secondary = Emas,
    onSecondary = Hijau,
    background = Hijau,
    onBackground = Krem,
    surface = Hijau,
    onSurface = Krem,
    surfaceVariant = HijauKartu,
    onSurfaceVariant = Krem
)

@Composable
fun MesjidUlulAlbabTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MasjidColorScheme,
        typography = Typography,
        content = content
    )
}
