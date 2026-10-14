package com.mesjidululalbab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.HijauMuda
import com.mesjidululalbab.ui.theme.Krem

@Composable
fun MenuTombol(
    ikon: String,
    judul: String,
    keterangan: String,
    badgeText: String? = null,
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(HijauMuda),
                contentAlignment = Alignment.Center
            ) {
                Text(ikon, fontSize = 26.sp)
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = judul,
                        color = Emas,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (!badgeText.isNullOrBlank()) {
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            color = Emas,
                            shape = CircleShape
                        ) {
                            Text(
                                text = badgeText,
                                color = Hijau,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = keterangan,
                    color = Krem,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Emas,
                modifier = Modifier.size(24.dp)
            )
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
                keterangan = "Tambah, edit, dan kelola agenda kegiatan",
                badgeText = "3 Agenda",
                aksi = {}
            )
        }
    }
}
