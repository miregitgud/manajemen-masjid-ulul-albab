package com.mesjidululalbab.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenRoute : NavKey {
    val title: String

    @Serializable
    data object Beranda : ScreenRoute {
        override val title: String = "Beranda"
    }

    @Serializable
    data object Jadwal : ScreenRoute {
        override val title: String = "Jadwal"
    }

    @Serializable
    data object Infak : ScreenRoute {
        override val title: String = "Infak"
    }

    @Serializable
    data object Berita : ScreenRoute {
        override val title: String = "Berita"
    }
}
