package com.mesjidululalbab.ui

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mesjidululalbab.ui.navigation.AppNavigation
import com.mesjidululalbab.ui.screens.SplashScreen
import com.mesjidululalbab.ui.theme.MesjidUlulAlbabTheme

@Composable
fun AplikasiMasjid(
    context: Context = LocalContext.current
) {
    var showSplashScreen by remember { mutableStateOf(true) }

    AnimatedContent(
        targetState = showSplashScreen,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(700))
        },
        label = "SplashTransition"
    ) { isSplash ->
        if (isSplash) {
            SplashScreen(
                onTimeout = { showSplashScreen = false }
            )
        } else {
            AppNavigation(context = context)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AplikasiMasjidPreview() {
    MesjidUlulAlbabTheme {
        AplikasiMasjid()
    }
}
