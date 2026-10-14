package com.mesjidululalbab.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mesjidululalbab.data.model.Jadwal

@Composable
fun JadwalFormDialog(
    jadwalAwal: Jadwal?,
    onDismiss: () -> Unit,
    onSimpan: (Jadwal) -> Unit
) {
    var kategori by remember { mutableStateOf(jadwalAwal?.kategori ?: "Harian") }
    var nama by remember { mutableStateOf(jadwalAwal?.nama ?: "") }
    var tanggal by remember { mutableStateOf(jadwalAwal?.tanggal ?: "") }
    var waktu by remember { mutableStateOf(jadwalAwal?.waktu ?: "") }
    var tempat by remember { mutableStateOf(jadwalAwal?.tempat ?: "Masjid Ulul Albab") }
    var petugas by remember { mutableStateOf(jadwalAwal?.petugas ?: "") }
    var keterangan by remember { mutableStateOf(jadwalAwal?.keterangan ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (jadwalAwal != null) "Edit Jadwal" else "Tambah Jadwal")
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 480.dp)
            ) {
                item {
                    Text("Kategori", fontWeight = FontWeight.Bold)
                    listOf("Harian", "Pekanan", "Bulanan", "Tahunan")
                        .forEach { pilihan ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = kategori == pilihan,
                                    onClick = { kategori = pilihan }
                                )
                                Text(pilihan)
                            }
                        }
                }
                item {
                    KolomInput("Nama kegiatan *", nama) { nama = it }
                    KolomInput("Tanggal (contoh: 20/10/2026)", tanggal) {
                        tanggal = it
                    }
                    KolomInput("Waktu (contoh: 08.00 WIB)", waktu) {
                        waktu = it
                    }
                    KolomInput("Tempat", tempat) { tempat = it }
                    KolomInput("Penceramah / petugas", petugas) {
                        petugas = it
                    }
                    KolomInput("Keterangan", keterangan) {
                        keterangan = it
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nama.isNotBlank() && tanggal.isNotBlank()) {
                        onSimpan(
                            Jadwal(
                                kategori = kategori,
                                nama = nama.trim(),
                                tanggal = tanggal.trim(),
                                waktu = waktu.trim(),
                                tempat = tempat.trim(),
                                petugas = petugas.trim(),
                                keterangan = keterangan.trim()
                            )
                        )
                    }
                }
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Preview
@Composable
fun JadwalFormDialogPreview() {
    JadwalFormDialog(
        jadwalAwal = null,
        onDismiss = {},
        onSimpan = {}
    )
}
