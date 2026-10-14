package com.mesjidululalbab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mesjidululalbab.ui.AplikasiMasjid
import com.mesjidululalbab.ui.theme.MesjidUlulAlbabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MesjidUlulAlbabTheme {
                AplikasiMasjid(this)
            }
        }
    }
}
