package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.components.MenuTombol
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.Krem

@Composable
fun BerandaScreen(
    onNavigasiKe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "PENGELOLAAN MASJID",
            color = Krem,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(18.dp))

        MenuTombol(
            ikon = "📅",
            judul = "Jadwal Kegiatan",
            keterangan = "Tambah, edit, dan kelola agenda",
            aksi = { onNavigasiKe("Jadwal") }
        )
        MenuTombol(
            ikon = "💰",
            judul = "Infak Jumat",
            keterangan = "Pencatatan jumlah infak",
            aksi = { onNavigasiKe("Infak") }
        )
        MenuTombol(
            ikon = "📋",
            judul = "Berita Acara",
            keterangan = "Dokumen dengan empat orang saksi",
            aksi = { onNavigasiKe("Berita") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    Surface(color = Hijau) {
        BerandaScreen(onNavigasiKe = {})
    }
}
