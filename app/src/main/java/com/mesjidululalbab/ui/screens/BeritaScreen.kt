package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.ui.components.BeritaAcaraDetailDialog
import com.mesjidululalbab.ui.components.BeritaAcaraFormDialog
import com.mesjidululalbab.ui.components.BeritaAcaraHapusDialog
import com.mesjidululalbab.ui.components.KartuBeritaAcara
import com.mesjidululalbab.ui.theme.*

@Composable
fun BeritaScreen(
    daftarBerita: List<BeritaAcara>,
    onTambahBerita: (BeritaAcara) -> Unit,
    onEditBerita: (Int, BeritaAcara) -> Unit,
    onHapusBerita: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var editIndex by remember { mutableIntStateOf(-1) }
    var showFormDialog by remember { mutableStateOf(false) }
    var detailIndex by remember { mutableIntStateOf(-1) }
    var hapusIndex by remember { mutableIntStateOf(-1) }

    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(daftarBerita.toList(), searchQuery) {
        daftarBerita.mapIndexed { originalIndex, item -> originalIndex to item }
            .filter { (_, item) ->
                searchQuery.isBlank() ||
                        item.nomorSurat.contains(searchQuery, ignoreCase = true) ||
                        item.tanggal.contains(searchQuery, ignoreCase = true) ||
                        item.saksi1.contains(searchQuery, ignoreCase = true) ||
                        item.saksi2.contains(searchQuery, ignoreCase = true) ||
                        item.saksi3.contains(searchQuery, ignoreCase = true) ||
                        item.saksi4.contains(searchQuery, ignoreCase = true) ||
                        item.keterangan.contains(searchQuery, ignoreCase = true)
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
            Text("+ Buat Berita Acara Baru")
        }

        Spacer(Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari no. dokumen, saksi, tanggal...") },
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

        Spacer(Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            Text(
                text = if (daftarBerita.isEmpty())
                    "Belum ada dokumen Berita Acara. Tekan tombol di atas untuk membuat Berita Acara baru."
                else
                    "Tidak ada Berita Acara yang cocok dengan pencarian.",
                color = Krem,
                modifier = Modifier.padding(12.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { (_, item) -> item.id }) { (origIdx, item) ->
                    KartuBeritaAcara(
                        berita = item,
                        onLihatDokumen = { detailIndex = origIdx },
                        onEdit = { bukaForm(origIdx) },
                        onHapus = { hapusIndex = origIdx }
                    )
                }
            }
        }
    }

    if (showFormDialog) {
        val beritaEdit = if (editIndex in daftarBerita.indices) daftarBerita[editIndex] else null
        BeritaAcaraFormDialog(
            beritaAwal = beritaEdit,
            onDismiss = { showFormDialog = false },
            onSimpan = { baru ->
                if (editIndex >= 0) {
                    onEditBerita(editIndex, baru)
                } else {
                    onTambahBerita(baru)
                }
                showFormDialog = false
            }
        )
    }

    if (detailIndex in daftarBerita.indices) {
        BeritaAcaraDetailDialog(
            berita = daftarBerita[detailIndex],
            onDismiss = { detailIndex = -1 }
        )
    }

    if (hapusIndex in daftarBerita.indices) {
        BeritaAcaraHapusDialog(
            onDismiss = { hapusIndex = -1 },
            onKonfirmasiHapus = {
                onHapusBerita(hapusIndex)
                hapusIndex = -1
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BeritaScreenPreview() {
    Surface(color = Hijau) {
        BeritaScreen(
            daftarBerita = listOf(
                BeritaAcara(
                    id = "1",
                    nomorSurat = "BA/001/MUA/X/2026",
                    hari = "Jumat",
                    tanggal = "24/10/2026",
                    jumlahInfak = 5500000L,
                    terbilang = "Lima Juta Lima Ratus Ribu Rupiah",
                    saksi1 = "H. Ahmad (Ketua DKM)",
                    saksi2 = "H. Ridwan (Bendahara)",
                    saksi3 = "Ustadz Faisal",
                    saksi4 = "Ir. Bambang",
                    keterangan = "Hasil Shalat Jumat"
                )
            ),
            onTambahBerita = {},
            onEditBerita = { _, _ -> },
            onHapusBerita = {}
        )
    }
}
