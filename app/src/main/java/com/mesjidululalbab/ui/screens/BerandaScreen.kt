package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.ui.components.KartuJadwalShalat
import com.mesjidululalbab.ui.components.MenuTombol
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.formatRupiah

@Composable
fun BerandaScreen(
    jadwalTerdekat: Jadwal? = null,
    totalUpcomingJadwal: Int = 0,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sleek Islamic Greeting Banner (INFORMATIONAL HEADER)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = HijauKartu.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, emasGradient),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "السلام عليكم ورحمة الله وبركاته",
                            color = EmasTerang,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Assalamu'alaikum Warahmatullahi Wabarakatuh",
                            color = Krem.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("☪", fontSize = 28.sp)
                }
            }

            // Prayer Times Compact Widget (CLICKABLE FOR DETAIL & LOCATION)
            KartuJadwalShalat()

            // Quick Summary Metrics Row (PURE NON-CLICKABLE STAT SUMMARY)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    label = "Agenda",
                    value = "$totalUpcomingJadwal",
                    unit = "Mendatang",
                    isHighlight = false,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Saldo Kas",
                    value = formatRupiah(saldoInfak),
                    unit = "Infak Jumat",
                    isHighlight = true,
                    modifier = Modifier.weight(1.3f)
                )
                StatCard(
                    label = "Berita Acara",
                    value = "$totalBerita",
                    unit = "Dokumen",
                    isHighlight = false,
                    modifier = Modifier.weight(1f)
                )
            }

            // Featured "Agenda Terdekat" Section (HIGH CONTRAST HIGHLIGHT PREVIEW)
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = EmasTerang,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "AGENDA TERDEKAT",
                        color = EmasTerang,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                // Vibrant Interactive Highlight Card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = HijauKartuHighlight
                    ),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.2.dp, EmasTerang),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigasiKe("Jadwal") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (jadwalTerdekat != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = EmasTerang,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = jadwalTerdekat.kategori,
                                        color = HijauGelap,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
                                    )
                                }
                                val tglText = if (jadwalTerdekat.kategori == "Harian") {
                                    "${jadwalTerdekat.tanggal} • ${jadwalTerdekat.waktu}"
                                } else {
                                    "Berikutnya: ${com.mesjidululalbab.util.hitungTanggalBerikutnyaFormatted(jadwalTerdekat)} • ${jadwalTerdekat.waktu}"
                                }
                                Text(
                                    text = tglText,
                                    color = Krem,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = jadwalTerdekat.nama,
                                color = EmasTerang,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )

                            Spacer(Modifier.height(2.dp))

                            Text(
                                text = "Tempat: ${jadwalTerdekat.tempat}",
                                color = Krem,
                                fontSize = 12.sp
                            )

                            if (jadwalTerdekat.petugas.isNotBlank()) {
                                Text(
                                    text = "Petugas/Penceramah: ${jadwalTerdekat.petugas}",
                                    color = Krem,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    color = EmasTerang,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Buka Detail Agenda",
                                            color = HijauGelap,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = HijauGelap,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Belum ada agenda mendatang.",
                                color = Krem,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(4.dp))
                            TextButton(
                                onClick = { onNavigasiKe("Jadwal") },
                                colors = ButtonDefaults.textButtonColors(contentColor = EmasTerang)
                            ) {
                                Text("+ Tambah Agenda Baru", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Core Menu Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Emas,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "MENU UTAMA",
                    color = Krem,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            // Core Menu Cards (PRIMARY NAVIGATION MODULES)
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                MenuTombol(
                    ikon = "📅",
                    judul = "Jadwal Kegiatan",
                    keterangan = "Tambah, edit, dan kelola agenda kegiatan masjid",
                    badgeText = if (totalUpcomingJadwal > 0) "$totalUpcomingJadwal Mendatang" else null,
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
    isHighlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) HijauKartuHighlight else HijauKartu
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            if (isHighlight) 1.2.dp else 0.8.dp,
            if (isHighlight) EmasTerang else Emas.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlight) 3.dp else 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(label, color = if (isHighlight) Krem else Krem.copy(alpha = 0.8f), fontSize = 10.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                color = if (isHighlight) EmasTerang else Emas,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1
            )
            Text(unit, color = Krem, fontSize = 9.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    Surface(color = Hijau) {
        BerandaScreen(
            jadwalTerdekat = Jadwal(
                kategori = "Harian",
                nama = "Kultum Subuh Berjamaah",
                tanggal = "24/10/2026",
                waktu = "05.00 WIB",
                tempat = "Masjid Ulul Albab",
                petugas = "Ustadz Ahmad",
                keterangan = "Membahas Kitab Riyadush Shalihin Bab Keikhlasan"
            ),
            totalUpcomingJadwal = 3,
            saldoInfak = 5250000L,
            totalBerita = 2,
            onNavigasiKe = {}
        )
    }
}
