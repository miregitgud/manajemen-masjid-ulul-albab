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
import com.mesjidululalbab.data.model.InfakJumat
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.formatRupiah

@Composable
fun KartuInfak(
    infak: InfakJumat,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = infak.kategori,
                    color = Emas,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = infak.tanggal,
                    color = Krem,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(6.dp))

            if (infak.jumlahPengeluaran > 0) {
                Text(
                    text = "Pengeluaran: ${formatRupiah(infak.jumlahPengeluaran)}",
                    color = MerahHapus,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = "Pemasukan: ${formatRupiah(infak.jumlahPemasukan)}",
                    color = Krem,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            if (infak.petugasPenghitung.isNotBlank()) {
                Text(
                    text = "Petugas/Saksi: ${infak.petugasPenghitung}",
                    color = Krem,
                    fontSize = 12.sp
                )
            }

            if (infak.keterangan.isNotBlank()) {
                Text(
                    text = infak.keterangan,
                    color = Krem,
                    fontSize = 12.sp
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
fun KartuInfakPreview() {
    Surface(color = Hijau) {
        Box(modifier = Modifier.padding(16.dp)) {
            KartuInfak(
                infak = InfakJumat(
                    id = "1",
                    tanggal = "24/10/2026",
                    kategori = "Kotak Infak Jumat",
                    jumlahPemasukan = 5500000L,
                    jumlahPengeluaran = 250000L,
                    petugasPenghitung = "H. Ahmad & H. Ridwan",
                    keterangan = "Hasil perhitungan Ba'da Shalat Jumat"
                ),
                onEdit = {},
                onHapus = {}
            )
        }
    }
}
