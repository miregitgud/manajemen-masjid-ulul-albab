package com.mesjidululalbab.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.JadwalShalatItem
import com.mesjidululalbab.util.LocationRepository
import com.mesjidululalbab.util.LokasiShalat
import com.mesjidululalbab.util.PrayerTimeUtils
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun KartuJadwalShalat(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lokasiAktif by remember {
        mutableStateOf(LocationRepository.muatLokasi(context))
    }
    var showLocationPicker by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }

    val jadwalShalat = remember(lokasiAktif) {
        PrayerTimeUtils.getJadwalShalatHariIni(
            latitude = lokasiAktif.latitude,
            longitude = lokasiAktif.longitude
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = HijauKartu
        ),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, Emas.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDetailDialog = true }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "JADWAL SHALAT HARI INI",
                    color = Krem,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                // Location Picker Chip Button on Top Right Corner
                Surface(
                    color = HijauMuda,
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Emas.copy(alpha = 0.6f)),
                    modifier = Modifier.clickable { showLocationPicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Pilih Lokasi",
                            tint = EmasTerang,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = lokasiAktif.namaKota,
                            color = EmasTerang,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(jadwalShalat) { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isNext) Emas else HijauMuda
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(82.dp)
                            .then(
                                if (item.isNext) Modifier.border(1.dp, EmasTerang, RoundedCornerShape(12.dp)) else Modifier
                            )
                            .clickable { showDetailDialog = true }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.nama,
                                color = if (item.isNext) Hijau else Krem,
                                fontSize = 11.sp,
                                fontWeight = if (item.isNext) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = item.waktu,
                                color = if (item.isNext) Hijau else EmasTerang,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (item.isNext) {
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "Berikutnya",
                                    color = Hijau,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDetailDialog) {
        JadwalShalatDetailDialog(
            lokasi = lokasiAktif,
            jadwalShalat = jadwalShalat,
            onDismiss = { showDetailDialog = false },
            onUbahLokasiClick = {
                showDetailDialog = false
                showLocationPicker = true
            }
        )
    }

    if (showLocationPicker) {
        LokasiPickerDialog(
            lokasiAktif = lokasiAktif,
            onDismiss = { showLocationPicker = false },
            onPilihLokasi = { baru ->
                lokasiAktif = baru
                LocationRepository.simpanLokasi(context, baru)
                showLocationPicker = false
            }
        )
    }
}

@Composable
fun JadwalShalatDetailDialog(
    lokasi: LokasiShalat,
    jadwalShalat: List<JadwalShalatItem>,
    onDismiss: () -> Unit,
    onUbahLokasiClick: () -> Unit
) {
    val sdfTanggal = remember { SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("id-ID")) }
    val tanggalFormatted = remember { sdfTanggal.format(Date()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Hijau,
                    contentColor = Emas
                )
            ) {
                Text("Tutup")
            }
        },
        dismissButton = {
            TextButton(onClick = onUbahLokasiClick) {
                Text("📍 Ubah Lokasi", color = Emas)
            }
        },
        title = {
            Column {
                Text(
                    text = "Jadwal Shalat Lengkap",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = "${lokasi.namaKota} • $tanggalFormatted",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                jadwalShalat.forEach { item ->
                    val ikonWaktu = when (item.nama) {
                        "Subuh" -> "🌅"
                        "Syuruq" -> "☀️"
                        "Dzuhur" -> "☀️"
                        "Ashar" -> "⛅"
                        "Maghrib" -> "🌅"
                        else -> "🌙"
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isNext) HijauKartuHighlight else HijauKartu
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = if (item.isNext) androidx.compose.foundation.BorderStroke(1.dp, EmasTerang) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ikonWaktu, fontSize = 20.sp)
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.nama,
                                        color = if (item.isNext) EmasTerang else Krem,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    if (item.isNext) {
                                        Text(
                                            text = "★ Waktu Shalat Berikutnya",
                                            color = Emas,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "${item.waktu} WIB",
                                color = if (item.isNext) EmasTerang else Krem,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun KartuJadwalShalatPreview() {
    Surface(color = Hijau) {
        Box(modifier = Modifier.padding(16.dp)) {
            KartuJadwalShalat()
        }
    }
}
