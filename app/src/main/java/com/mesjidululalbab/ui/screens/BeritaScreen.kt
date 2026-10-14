package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.Krem

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

        Spacer(Modifier.height(14.dp))

        if (daftarBerita.isEmpty()) {
            Text(
                text = "Belum ada dokumen Berita Acara. Tekan tombol di atas untuk membuat Berita Acara baru.",
                color = Krem,
                modifier = Modifier.padding(12.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(daftarBerita) { index, item ->
                    KartuBeritaAcara(
                        berita = item,
                        onLihatDokumen = { detailIndex = index },
                        onEdit = { bukaForm(index) },
                        onHapus = { hapusIndex = index }
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
                ),
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
                ),
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
                ),
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
                ),
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
                ),
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
