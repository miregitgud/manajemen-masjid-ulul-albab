package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.ui.components.JadwalFormDialog
import com.mesjidululalbab.ui.components.JadwalHapusDialog
import com.mesjidululalbab.ui.components.KartuJadwal
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.Krem

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

        Spacer(Modifier.height(12.dp))

        if (daftarJadwal.isEmpty()) {
            Text(
                text = "Belum ada jadwal. Tekan Tambah Jadwal untuk memulai.",
                color = Krem,
                modifier = Modifier.padding(12.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(daftarJadwal) { index, item ->
                    KartuJadwal(
                        jadwal = item,
                        onEdit = { bukaForm(index) },
                        onHapus = { hapusIndex = index }
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
