package com.mesjidululalbab.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.hitungTanggalBerikutnyaFormatted

@Composable
fun JadwalDetailDialog(
    jadwal: Jadwal,
    onDismiss: () -> Unit,
    onKelolaJadwalClick: () -> Unit
) {
    val tglText = if (jadwal.kategori == "Harian") {
        "${jadwal.tanggal} • ${jadwal.waktu}"
    } else {
        "Pelaksanaan Berikutnya: ${hitungTanggalBerikutnyaFormatted(jadwal)} • ${jadwal.waktu}"
    }

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
        dismissButton = { },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Detail Agenda Kegiatan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Surface(
                    color = Emas,
                    shape = CircleShape
                ) {
                    Text(
                        text = jadwal.kategori,
                        color = HijauGelap,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        },
        text = {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Krem
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Emas),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Title
                    Text(
                        text = jadwal.nama,
                        color = HijauGelap,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    HorizontalDivider(color = Emas, thickness = 1.dp)

                    // Date & Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Hijau,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = tglText,
                            color = HijauGelap,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }

                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Hijau,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = jadwal.tempat,
                            color = HijauGelap,
                            fontSize = 13.sp
                        )
                    }

                    // Officer / Speaker
                    if (jadwal.petugas.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Hijau,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Petugas/Penceramah: ${jadwal.petugas}",
                                color = HijauGelap,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Description
                    if (jadwal.keterangan.isNotBlank()) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Hijau,
                                modifier = Modifier.size(18.dp).padding(top = 2.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = jadwal.keterangan,
                                color = HijauGelap.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
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
fun JadwalDetailDialogPreview() {
    JadwalDetailDialog(
        jadwal = Jadwal(
            kategori = "Harian",
            nama = "Kultum Subuh Berjamaah",
            tanggal = "24/10/2026",
            waktu = "05.00 WIB",
            tempat = "Masjid Ulul Albab",
            petugas = "Ustadz Ahmad",
            keterangan = "Membahas Kitab Riyadush Shalihin Bab Keikhlasan"
        ),
        onDismiss = {},
        onKelolaJadwalClick = {}
    )
}
