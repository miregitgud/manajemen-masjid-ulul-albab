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
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.formatRupiah

@Composable
fun KartuBeritaAcara(
    berita: BeritaAcara,
    onLihatDokumen: () -> Unit,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val signedCount = listOf(berita.saksi1Ttd, berita.saksi2Ttd, berita.saksi3Ttd, berita.saksi4Ttd)
        .count { it.isNotBlank() }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = berita.nomorSurat,
                    color = Emas,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "${berita.hari}, ${berita.tanggal}",
                    color = Krem,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = formatRupiah(berita.jumlahInfak),
                color = Krem,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = "\"${berita.terbilang}\"",
                color = Emas,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Empat Saksi Penandatangan:",
                color = Krem,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "1. ${berita.saksi1}  •  2. ${berita.saksi2}\n3. ${berita.saksi3}  •  4. ${berita.saksi4}",
                color = Krem,
                fontSize = 11.sp
            )

            Spacer(Modifier.height(6.dp))

            Surface(
                color = if (signedCount == 4) HijauMuda else HijauAksen,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "✍ Status Ttd Digital: $signedCount / 4 Saksi Terrekam",
                    color = EmasTerang,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onLihatDokumen,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emas,
                        contentColor = Hijau
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("📄 Lihat Dokumen")
                }

                Spacer(Modifier.width(8.dp))

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
}

@Preview(showBackground = true)
@Composable
fun KartuBeritaAcaraPreview() {
    Surface(color = Hijau) {
        Box(modifier = Modifier.padding(16.dp)) {
            KartuBeritaAcara(
                berita = BeritaAcara(
                    id = "1",
                    nomorSurat = "BA/001/MUA/X/2026",
                    hari = "Jumat",
                    tanggal = "24/10/2026",
                    jumlahInfak = 5500000L,
                    terbilang = "Lima Juta Lima Ratus Ribu Rupiah",
                    saksi1 = "H. Ahmad (Ketua DKM)",
                    saksi2 = "H. Ridwan (Bendahara)",
                    saksi3 = "Ustadz Faisal",
                    saksi4 = "Ir. Bambang",
                    keterangan = "Hasil Shalat Jumat"
                ),
                onLihatDokumen = {},
                onEdit = {},
                onHapus = {}
            )
        }
    }
}
