package com.mesjidululalbab.ui.navigation

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.data.repository.JadwalRepository
import com.mesjidululalbab.ui.components.MasjidBottomBar
import com.mesjidululalbab.ui.components.MasjidTopBar
import com.mesjidululalbab.ui.screens.BerandaScreen
import com.mesjidululalbab.ui.screens.BeritaScreen
import com.mesjidululalbab.ui.screens.InfakScreen
import com.mesjidululalbab.ui.screens.JadwalScreen
import com.mesjidululalbab.ui.theme.Hijau

@Composable
fun AppNavigation(
    context: Context = LocalContext.current
) {
    val repository = remember(context) { JadwalRepository(context) }
    val daftarJadwal = remember {
        mutableStateListOf<Jadwal>().apply {
            addAll(repository.muatJadwal())
        }
    }

    val backStack = rememberNavBackStack(ScreenRoute.Beranda)
    val ruteAktif = backStack.lastOrNull() as? ScreenRoute ?: ScreenRoute.Beranda
    val halamanAktifName = when (ruteAktif) {
        ScreenRoute.Beranda -> "Beranda"
        ScreenRoute.Jadwal -> "Jadwal"
        ScreenRoute.Infak -> "Infak"
        ScreenRoute.Berita -> "Berita"
    }

    val bisaKembali = backStack.size > 1

    BackHandler(enabled = bisaKembali) {
        backStack.removeLastOrNull()
    }

    fun navigasiKe(ruteBaru: ScreenRoute) {
        if (ruteAktif != ruteBaru) {
            if (ruteBaru == ScreenRoute.Beranda) {
                backStack.clear()
                backStack.add(ScreenRoute.Beranda)
            } else {
                if (backStack.contains(ruteBaru)) {
                    backStack.remove(ruteBaru)
                }
                backStack.add(ruteBaru)
            }
        }
    }

    Scaffold(
        containerColor = Hijau,
        topBar = {
            MasjidTopBar(
                halamanAktif = halamanAktifName,
                bisaKembali = bisaKembali,
                onKembaliClick = { backStack.removeLastOrNull() }
            )
        },
        bottomBar = {
            MasjidBottomBar(
                halamanAktif = halamanAktifName,
                onPilihHalaman = { id ->
                    val ruteTarget = when (id) {
                        "Jadwal" -> ScreenRoute.Jadwal
                        "Infak" -> ScreenRoute.Infak
                        "Berita" -> ScreenRoute.Berita
                        else -> ScreenRoute.Beranda
                    }
                    navigasiKe(ruteTarget)
                }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                // Smooth slide in horizontally + fade in for forward screen transition
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth / 3 },
                            animationSpec = tween(350)
                        ) + fadeOut(animationSpec = tween(350)))
            },
            popTransitionSpec = {
                // Smooth slide in from left + fade in for backward screen transition
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(350)
                        ) + fadeOut(animationSpec = tween(350)))
            },
            entryProvider = { key ->
                when (key) {
                    is ScreenRoute.Beranda -> NavEntry(key) {
                        BerandaScreen(
                            onNavigasiKe = { id ->
                                val ruteTarget = when (id) {
                                    "Jadwal" -> ScreenRoute.Jadwal
                                    "Infak" -> ScreenRoute.Infak
                                    "Berita" -> ScreenRoute.Berita
                                    else -> ScreenRoute.Beranda
                                }
                                navigasiKe(ruteTarget)
                            }
                        )
                    }

                    is ScreenRoute.Jadwal -> NavEntry(key) {
                        JadwalScreen(
                            daftarJadwal = daftarJadwal,
                            onTambahJadwal = { baru ->
                                daftarJadwal.add(baru)
                                repository.simpanJadwal(daftarJadwal.toList())
                            },
                            onEditJadwal = { index, baru ->
                                if (index in daftarJadwal.indices) {
                                    daftarJadwal[index] = baru
                                    repository.simpanJadwal(daftarJadwal.toList())
                                }
                            },
                            onHapusJadwal = { index ->
                                if (index in daftarJadwal.indices) {
                                    daftarJadwal.removeAt(index)
                                    repository.simpanJadwal(daftarJadwal.toList())
                                }
                            }
                        )
                    }

                    is ScreenRoute.Infak -> NavEntry(key) {
                        InfakScreen()
                    }

                    is ScreenRoute.Berita -> NavEntry(key) {
                        BeritaScreen()
                    }

                    else -> error("Rute tidak dikenal: $key")
                }
            }
        )
    }
}
