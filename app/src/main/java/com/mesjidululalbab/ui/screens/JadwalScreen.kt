package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.ui.components.JadwalFormDialog
import com.mesjidululalbab.ui.components.JadwalHapusDialog
import com.mesjidululalbab.ui.components.KartuJadwal
import com.mesjidululalbab.ui.theme.*

@Composable
fun JadwalScreen(
    daftarJadwal: List<Jadwal>,
    onTambahJadwal: (Jadwal) -> Unit,
    onEditJadwal: (Int, Jadwal) -> Unit,
    onHapusJadwal: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var editIndex by remember { mutableIntStateOf(-1) }
    var showFormDialog by remember { mutableStateOf(false) }
    var hapusIndex by remember { mutableIntStateOf(-1) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedKategoriFilter by remember { mutableStateOf("Semua") }

    val filteredList = remember(daftarJadwal.toList(), searchQuery, selectedKategoriFilter) {
        daftarJadwal.mapIndexed { originalIndex, item -> originalIndex to item }
            .filter { (_, item) ->
                val matchesKategori = selectedKategoriFilter == "Semua" || item.kategori.equals(selectedKategoriFilter, ignoreCase = true)
                val matchesQuery = searchQuery.isBlank() ||
                        item.nama.contains(searchQuery, ignoreCase = true) ||
                        item.petugas.contains(searchQuery, ignoreCase = true) ||
                        item.tempat.contains(searchQuery, ignoreCase = true) ||
                        item.keterangan.contains(searchQuery, ignoreCase = true)
                matchesKategori && matchesQuery
            }
    }

    fun bukaForm(index: Int = -1) {
        editIndex = index
        showFormDialog = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { bukaForm() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Emas,
                contentColor = Hijau
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Tambah Jadwal")
        }

        Spacer(Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari agenda, penceramah, tempat...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = HijauKartu,
                unfocusedContainerColor = HijauKartu,
                focusedTextColor = Krem,
                unfocusedTextColor = Krem
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // Filter Chips Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(listOf("Semua", "Harian", "Pekanan", "Bulanan", "Tahunan")) { kat ->
                FilterChip(
                    selected = selectedKategoriFilter == kat,
                    onClick = { selectedKategoriFilter = kat },
                    label = { Text(kat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Emas,
                        selectedLabelColor = Hijau,
                        containerColor = HijauKartu,
                        labelColor = Krem
                    )
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            Text(
                text = if (daftarJadwal.isEmpty())
                    "Belum ada jadwal. Tekan Tambah Jadwal untuk memulai."
                else
                    "Tidak ada jadwal yang cocok dengan pencarian / filter.",
                color = Krem,
                modifier = Modifier.padding(12.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { (origIdx, item) ->
                    KartuJadwal(
                        jadwal = item,
                        onEdit = { bukaForm(origIdx) },
                        onHapus = { hapusIndex = origIdx }
                    )
                }
            }
        }
    }

    if (showFormDialog) {
        val jadwalEdit = if (editIndex in daftarJadwal.indices) daftarJadwal[editIndex] else null
        JadwalFormDialog(
            jadwalAwal = jadwalEdit,
            onDismiss = { showFormDialog = false },
            onSimpan = { baru ->
                if (editIndex >= 0) {
                    onEditJadwal(editIndex, baru)
                } else {
                    onTambahJadwal(baru)
                }
                showFormDialog = false
            }
        )
    }

    if (hapusIndex >= 0) {
        JadwalHapusDialog(
            onDismiss = { hapusIndex = -1 },
            onKonfirmasiHapus = {
                onHapusJadwal(hapusIndex)
                hapusIndex = -1
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JadwalScreenPreview() {
    Surface(color = Hijau) {
        JadwalScreen(
            daftarJadwal = listOf(
                Jadwal(
                    kategori = "Harian",
                    nama = "Kultum Subuh",
                    tanggal = "20/10/2026",
                    waktu = "05.00 WIB",
                    tempat = "Masjid Ulul Albab",
                    petugas = "Ustadz Ahmad",
                    keterangan = "Kitab Riyadush Shalihin"
                )
            ),
            onTambahJadwal = {},
            onEditJadwal = { _, _ -> },
            onHapusJadwal = {}
        )
    }
}
