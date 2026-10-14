package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.components.MenuTombol
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.formatRupiah

@Composable
fun BerandaScreen(
    totalJadwal: Int = 0,
    saldoInfak: Long = 0L,
    totalBerita: Int = 0,
    onNavigasiKe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            HijauAksen,
            Hijau,
            HijauGelap
        )
    )

    val emasGradient = Brush.horizontalGradient(
        colors = listOf(
            Emas,
            EmasTerang,
            Emas
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Majestic Islamic Welcome Banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = HijauKartu.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, emasGradient),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    // Decorative Gold Accent Circle Background
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 20.dp, y = (-20).dp)
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Emas.copy(alpha = 0.08f))
                    )

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "☪",
                            color = EmasTerang,
                            fontSize = 28.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "السلام عليكم ورحمة الله وبركاته",
                            color = EmasTerang,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Assalamu'alaikum Warahmatullahi Wabarakatuh",
                            color = Krem.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic
                        )

                        Spacer(Modifier.height(14.dp))
                        HorizontalDivider(
                            color = Emas.copy(alpha = 0.4f),
                            thickness = 1.dp
                        )
                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "PENGELOLAAN MASJID ULUL ALBAB",
                            color = Emas,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "PT. Rekayasa Industri • Jakarta Selatan",
                            color = Krem,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Quick Overview Stats Row with Gold Trim
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    label = "Agenda",
                    value = "$totalJadwal",
                    unit = "Jadwal",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Saldo Kas",
                    value = formatRupiah(saldoInfak),
                    unit = "Infak Jumat",
                    modifier = Modifier.weight(1.3f)
                )
                StatCard(
                    label = "Berita Acara",
                    value = "$totalBerita",
                    unit = "Dokumen",
                    modifier = Modifier.weight(1f)
                )
            }

            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Emas,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "MENU UTAMA",
                    color = Krem,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Menu Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MenuTombol(
                    ikon = "📅",
                    judul = "Jadwal Kegiatan",
                    keterangan = "Tambah, edit, dan kelola agenda kegiatan masjid",
                    badgeText = if (totalJadwal > 0) "$totalJadwal Agenda" else null,
                    aksi = { onNavigasiKe("Jadwal") }
                )

                MenuTombol(
                    ikon = "💰",
                    judul = "Infak Jumat",
                    keterangan = "Pencatatan saldo kas penerimaan & pengeluaran infak",
                    badgeText = if (saldoInfak > 0L) formatRupiah(saldoInfak) else null,
                    aksi = { onNavigasiKe("Infak") }
                )

                MenuTombol(
                    ikon = "📋",
                    judul = "Berita Acara",
                    keterangan = "Dokumen berita acara resmi dengan empat (4) saksi",
                    badgeText = if (totalBerita > 0) "$totalBerita Dokumen" else null,
                    aksi = { onNavigasiKe("Berita") }
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.8.dp, Emas.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(label, color = Krem.copy(alpha = 0.8f), fontSize = 10.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                color = EmasTerang,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )
            Text(unit, color = Krem, fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    Surface(color = Hijau) {
        BerandaScreen(
            totalJadwal = 3,
            saldoInfak = 5250000L,
            totalBerita = 2,
            onNavigasiKe = {}
        )
    }
}
