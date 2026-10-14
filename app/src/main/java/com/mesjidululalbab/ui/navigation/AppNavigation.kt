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
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.data.model.InfakJumat
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.data.repository.BeritaAcaraRepository
import com.mesjidululalbab.data.repository.InfakRepository
import com.mesjidululalbab.data.repository.JadwalRepository
import com.mesjidululalbab.ui.components.BackupRestoreDialog
import com.mesjidululalbab.ui.components.MasjidTopBar
import com.mesjidululalbab.ui.screens.BerandaScreen
import com.mesjidululalbab.ui.screens.BeritaScreen
import com.mesjidululalbab.ui.screens.InfakScreen
import com.mesjidululalbab.ui.screens.JadwalScreen
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.util.isJadwalMendatang

@Composable
fun AppNavigation(
    context: Context = LocalContext.current
) {
    val jadwalRepository = remember(context) { JadwalRepository(context) }
    val daftarJadwal = remember {
        mutableStateListOf<Jadwal>().apply {
            addAll(jadwalRepository.muatJadwal())
        }
    }

    val infakRepository = remember(context) { InfakRepository(context) }
    val daftarInfak = remember {
        mutableStateListOf<InfakJumat>().apply {
            addAll(infakRepository.muatInfak())
        }
    }

    val beritaRepository = remember(context) { BeritaAcaraRepository(context) }
    val daftarBerita = remember {
        mutableStateListOf<BeritaAcara>().apply {
            addAll(beritaRepository.muatBeritaAcara())
        }
    }

    var showBackupDialog by remember { mutableStateOf(false) }

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
                onKembaliClick = { backStack.removeLastOrNull() },
                onBackupClick = { showBackupDialog = true }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300)))
            },
            popTransitionSpec = {
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300)))
            },
            predictivePopTransitionSpec = {
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300)))
            },
            entryProvider = { key ->
                when (key) {
                    is ScreenRoute.Beranda -> NavEntry(key) {
                        val upcomingJadwal = daftarJadwal.filter { isJadwalMendatang(it) }
                        val jadwalTerdekat = upcomingJadwal.firstOrNull() ?: daftarJadwal.firstOrNull()
                        val totalUpcomingJadwal = upcomingJadwal.size
                        val saldoInfak = daftarInfak.sumOf { it.jumlahPemasukan } - daftarInfak.sumOf { it.jumlahPengeluaran }
                        val totalBerita = daftarBerita.size

                        BerandaScreen(
                            jadwalTerdekat = jadwalTerdekat,
                            totalUpcomingJadwal = totalUpcomingJadwal,
                            saldoInfak = saldoInfak,
                            totalBerita = totalBerita,
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
                                jadwalRepository.simpanJadwal(daftarJadwal.toList())
                            },
                            onEditJadwal = { index, baru ->
                                if (index in daftarJadwal.indices) {
                                    daftarJadwal[index] = baru
                                    jadwalRepository.simpanJadwal(daftarJadwal.toList())
                                }
                            },
                            onHapusJadwal = { index ->
                                if (index in daftarJadwal.indices) {
                                    daftarJadwal.removeAt(index)
                                    jadwalRepository.simpanJadwal(daftarJadwal.toList())
                                }
                            }
                        )
                    }

                    is ScreenRoute.Infak -> NavEntry(key) {
                        InfakScreen(
                            daftarInfak = daftarInfak,
                            onTambahInfak = { baru ->
                                daftarInfak.add(baru)
                                infakRepository.simpanInfak(daftarInfak.toList())
                            },
                            onEditInfak = { index, baru ->
                                if (index in daftarInfak.indices) {
                                    daftarInfak[index] = baru
                                    infakRepository.simpanInfak(daftarInfak.toList())
                                }
                            },
                            onHapusInfak = { index ->
                                if (index in daftarInfak.indices) {
                                    daftarInfak.removeAt(index)
                                    infakRepository.simpanInfak(daftarInfak.toList())
                                }
                            }
                        )
                    }

                    is ScreenRoute.Berita -> NavEntry(key) {
                        BeritaScreen(
                            daftarBerita = daftarBerita,
                            onTambahBerita = { baru ->
                                daftarBerita.add(baru)
                                beritaRepository.simpanBeritaAcara(daftarBerita.toList())
                            },
                            onEditBerita = { index, baru ->
                                if (index in daftarBerita.indices) {
                                    daftarBerita[index] = baru
                                    beritaRepository.simpanBeritaAcara(daftarBerita.toList())
                                }
                            },
                            onHapusBerita = { index ->
                                if (index in daftarBerita.indices) {
                                    daftarBerita.removeAt(index)
                                    beritaRepository.simpanBeritaAcara(daftarBerita.toList())
                                }
                            }
                        )
                    }

                    else -> error("Rute tidak dikenal: $key")
                }
            }
        )
    }

    if (showBackupDialog) {
        BackupRestoreDialog(
            onDismiss = { showBackupDialog = false },
            onDataRestored = {
                daftarJadwal.clear()
                daftarJadwal.addAll(jadwalRepository.muatJadwal())
                daftarInfak.clear()
                daftarInfak.addAll(infakRepository.muatInfak())
                daftarBerita.clear()
                daftarBerita.addAll(beritaRepository.muatBeritaAcara())
                showBackupDialog = false
            }
        )
    }
}
