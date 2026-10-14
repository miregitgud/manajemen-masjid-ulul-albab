package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mesjidululalbab.data.model.InfakJumat
import com.mesjidululalbab.ui.components.InfakFormDialog
import com.mesjidululalbab.ui.components.InfakHapusDialog
import com.mesjidululalbab.ui.components.KartuInfak
import com.mesjidululalbab.ui.components.KartuRingkasanInfak
import com.mesjidululalbab.ui.theme.*

@Composable
fun InfakScreen(
    daftarInfak: List<InfakJumat>,
    onTambahInfak: (InfakJumat) -> Unit,
    onEditInfak: (Int, InfakJumat) -> Unit,
    onHapusInfak: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var editIndex by remember { mutableIntStateOf(-1) }
    var showFormDialog by remember { mutableStateOf(false) }
    var hapusIndex by remember { mutableIntStateOf(-1) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedKategoriFilter by remember { mutableStateOf("Semua") }

    val totalPemasukan = daftarInfak.sumOf { it.jumlahPemasukan }
    val totalPengeluaran = daftarInfak.sumOf { it.jumlahPengeluaran }

    val filteredList = remember(daftarInfak, searchQuery, selectedKategoriFilter) {
        daftarInfak.mapIndexed { originalIndex, item -> originalIndex to item }
            .filter { (_, item) ->
                val matchesKategori = selectedKategoriFilter == "Semua" || item.kategori.equals(selectedKategoriFilter, ignoreCase = true)
                val matchesQuery = searchQuery.isBlank() ||
                        item.kategori.contains(searchQuery, ignoreCase = true) ||
                        item.petugasPenghitung.contains(searchQuery, ignoreCase = true) ||
                        item.keterangan.contains(searchQuery, ignoreCase = true) ||
                        item.tanggal.contains(searchQuery, ignoreCase = true)
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
        KartuRingkasanInfak(
            totalPemasukan = totalPemasukan,
            totalPengeluaran = totalPengeluaran
        )

        Spacer(Modifier.height(14.dp))

        Button(
            onClick = { bukaForm() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Emas,
                contentColor = Hijau
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Catat Infak Baru")
        }

        Spacer(Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari kategori, petugas, keterangan...") },
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
            items(listOf("Semua", "Kotak Infak Jumat", "Transfer Bank", "QRIS", "Donatur Tetap")) { kat ->
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
                text = if (daftarInfak.isEmpty())
                    "Belum ada catatan infak. Tekan tombol di atas untuk mencatat infak."
                else
                    "Tidak ada catatan infak yang cocok dengan pencarian / filter.",
                color = Krem,
                modifier = Modifier.padding(12.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { (origIdx, item) ->
                    KartuInfak(
                        infak = item,
                        onEdit = { bukaForm(origIdx) },
                        onHapus = { hapusIndex = origIdx }
                    )
                }
            }
        }
    }

    if (showFormDialog) {
        val infakEdit = if (editIndex in daftarInfak.indices) daftarInfak[editIndex] else null
        InfakFormDialog(
            infakAwal = infakEdit,
            onDismiss = { showFormDialog = false },
            onSimpan = { baru ->
                if (editIndex >= 0) {
                    onEditInfak(editIndex, baru)
                } else {
                    onTambahInfak(baru)
                }
                showFormDialog = false
            }
        )
    }

    if (hapusIndex >= 0) {
        InfakHapusDialog(
            onDismiss = { hapusIndex = -1 },
            onKonfirmasiHapus = {
                onHapusInfak(hapusIndex)
                hapusIndex = -1
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfakScreenPreview() {
    Surface(color = Hijau) {
        InfakScreen(
            daftarInfak = listOf(
                InfakJumat(
                    id = "1",
                    tanggal = "24/10/2026",
                    kategori = "Kotak Infak Jumat",
                    jumlahPemasukan = 5500000L,
                    jumlahPengeluaran = 250000L,
                    petugasPenghitung = "H. Ahmad",
                    keterangan = "Hasil Shalat Jumat"
                )
            ),
            onTambahInfak = {},
            onEditInfak = { _, _ -> },
            onHapusInfak = {}
        )
    }
}
