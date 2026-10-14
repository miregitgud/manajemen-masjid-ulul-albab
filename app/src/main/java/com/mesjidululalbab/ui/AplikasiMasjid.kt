package com.mesjidululalbab.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mesjidululalbab.ui.navigation.AppNavigation
import com.mesjidululalbab.ui.theme.MesjidUlulAlbabTheme

@Composable
fun AplikasiMasjid(
    context: Context = LocalContext.current
) {
    AppNavigation(context = context)
}

@Preview(showBackground = true)
@Composable
fun AplikasiMasjidPreview() {
    MesjidUlulAlbabTheme {
        AplikasiMasjid()
    }
}
