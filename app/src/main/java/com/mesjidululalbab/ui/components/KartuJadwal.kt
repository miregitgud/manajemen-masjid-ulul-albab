package com.mesjidululalbab.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.hitungTanggalBerikutnyaFormatted

@Composable
fun KartuJadwal(
    jadwal: Jadwal,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tglBerikutnya = hitungTanggalBerikutnyaFormatted(jadwal)
    val infoFrekuensi = when (jadwal.kategori) {
        "Harian" -> "Sekali Jalan"
        "Pekanan" -> "Setiap Pekan"
        "Bulanan" -> "Setiap Bulan"
        "Tahunan" -> "Setiap Tahun"
        else -> "Jadwal"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            Modifier.padding(14.dp)
        ) {
            Text(
                jadwal.nama,
                color = Emas,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                if (jadwal.kategori == "Harian") {
                    "${jadwal.kategori} ($infoFrekuensi) • ${jadwal.tanggal} • ${jadwal.waktu}"
                } else {
                    "${jadwal.kategori} ($infoFrekuensi) • Berikutnya: $tglBerikutnya • ${jadwal.waktu}"
                },
                color = Krem,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Tempat: ${jadwal.tempat}",
                color = Krem
            )
            if (jadwal.petugas.isNotBlank()) {
                Text(
                    "Penceramah/Petugas: ${jadwal.petugas}",
                    color = Krem
                )
            }
            if (jadwal.keterangan.isNotBlank()) {
                Text(
                    jadwal.keterangan,
                    color = Krem
                )
            }
            Row {
                TextButton(onClick = onEdit) {
                    Text("Edit", color = Emas)
                }
                TextButton(onClick = onHapus) {
                    Text("Hapus", color = MerahHapus)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KartuJadwalPreview() {
    Surface(color = Hijau) {
        Box(modifier = Modifier.padding(16.dp)) {
            KartuJadwal(
                jadwal = Jadwal(
                    kategori = "Harian",
                    nama = "Kultum Subuh",
                    tanggal = "20/10/2026",
                    waktu = "05.00 WIB",
                    tempat = "Masjid Ulul Albab",
                    petugas = "Ustadz Ahmad",
                    keterangan = "Membahas Kitab Riyadush Shalihin"
                ),
                onEdit = {},
                onHapus = {}
            )
        }
    }
}
