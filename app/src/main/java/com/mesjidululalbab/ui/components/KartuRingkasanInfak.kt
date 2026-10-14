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
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.Krem
import com.mesjidululalbab.util.formatRupiah

@Composable
fun KartuRingkasanInfak(
    totalPemasukan: Long,
    totalPengeluaran: Long,
    modifier: Modifier = Modifier
) {
    val saldo = totalPemasukan - totalPengeluaran

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "RINGKASAN INFAK JUMAT",
                color = Krem,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = formatRupiah(saldo),
                color = Emas,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Saldo Akhir Kas Infak",
                color = Krem,
                fontSize = 11.sp
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = Hijau, thickness = 1.dp)
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Pemasukan", color = Krem, fontSize = 11.sp)
                    Text(
                        formatRupiah(totalPemasukan),
                        color = Krem,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Column {
                    Text("Total Pengeluaran", color = Krem, fontSize = 11.sp)
                    Text(
                        formatRupiah(totalPengeluaran),
                        color = MaterialTheme.colorScheme.errorContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KartuRingkasanInfakPreview() {
    Surface(color = Hijau) {
        Box(modifier = Modifier.padding(16.dp)) {
            KartuRingkasanInfak(
                totalPemasukan = 12500000L,
                totalPengeluaran = 2300000L
            )
        }
    }
}
