package com.mesjidululalbab.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.EmasTerang
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.Krem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasjidTopBar(
    halamanAktif: String,
    bisaKembali: Boolean,
    onKembaliClick: () -> Unit,
    onBackupClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = when (halamanAktif) {
                        "Beranda" -> "☪  MASJID ULUL ALBAB"
                        "Jadwal" -> "JADWAL KEGIATAN"
                        "Infak" -> "INFAK JUMAT"
                        else -> "BERITA ACARA"
                    },
                    color = Emas,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (halamanAktif == "Beranda") 20.sp else 18.sp
                )
                if (halamanAktif == "Beranda") {
                    Text(
                        text = "PT. Rekayasa Industri • Jakarta Selatan",
                        color = Krem,
                        fontSize = 11.sp
                    )
                }
            }
        },
        navigationIcon = {
            if (bisaKembali) {
                IconButton(onClick = onKembaliClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali ke Beranda",
                        tint = Emas
                    )
                }
            }
        },
        actions = {
            if (onBackupClick != null) {
                IconButton(onClick = onBackupClick) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = "Backup & Restore Data",
                        tint = EmasTerang
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Hijau
        )
    )
}

@Preview
@Composable
fun MasjidTopBarBerandaPreview() {
    MasjidTopBar(
        halamanAktif = "Beranda",
        bisaKembali = false,
        onKembaliClick = {},
        onBackupClick = {}
    )
}

@Preview
@Composable
fun MasjidTopBarSubScreenPreview() {
    MasjidTopBar(
        halamanAktif = "Jadwal",
        bisaKembali = true,
        onKembaliClick = {}
    )
}
