package com.mesjidululalbab.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.Krem

@Composable
fun MenuTombol(
    ikon: String,
    judul: String,
    keterangan: String,
    aksi: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { aksi() },
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(ikon, fontSize = 30.sp)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    judul,
                    color = Emas,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(keterangan, color = Krem, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuTombolPreview() {
    Surface(color = Hijau) {
        Box(modifier = Modifier.padding(16.dp)) {
            MenuTombol(
                ikon = "📅",
                judul = "Jadwal Kegiatan",
                keterangan = "Tambah, edit, dan kelola agenda",
                aksi = {}
            )
        }
    }
}
