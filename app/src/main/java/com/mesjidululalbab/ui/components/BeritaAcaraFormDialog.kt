package com.mesjidululalbab.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.Krem
import com.mesjidululalbab.util.TerbilangUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeritaAcaraFormDialog(
    beritaAwal: BeritaAcara?,
    onDismiss: () -> Unit,
    onSimpan: (BeritaAcara) -> Unit
) {
    var nomorSurat by remember {
        mutableStateOf(
            beritaAwal?.nomorSurat ?: "BA/${(100..999).random()}/MUA/${Calendar.getInstance().get(Calendar.YEAR)}"
        )
    }
    var hari by remember { mutableStateOf(beritaAwal?.hari ?: "Jumat") }
    var tanggal by remember { mutableStateOf(beritaAwal?.tanggal ?: "") }
    var jumlahText by remember { mutableStateOf(beritaAwal?.jumlahInfak?.toString() ?: "") }
    var saksi1 by remember { mutableStateOf(beritaAwal?.saksi1 ?: "") }
    var saksi2 by remember { mutableStateOf(beritaAwal?.saksi2 ?: "") }
    var saksi3 by remember { mutableStateOf(beritaAwal?.saksi3 ?: "") }
    var saksi4 by remember { mutableStateOf(beritaAwal?.saksi4 ?: "") }

    var saksi1Ttd by remember { mutableStateOf(beritaAwal?.saksi1Ttd ?: "") }
    var saksi2Ttd by remember { mutableStateOf(beritaAwal?.saksi2Ttd ?: "") }
    var saksi3Ttd by remember { mutableStateOf(beritaAwal?.saksi3Ttd ?: "") }
    var saksi4Ttd by remember { mutableStateOf(beritaAwal?.saksi4Ttd ?: "") }

    var keterangan by remember { mutableStateOf(beritaAwal?.keterangan ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    var signatureIndexToEdit by remember { mutableStateOf<Int?>(null) }

    val jumlah = jumlahText.toLongOrNull() ?: 0L
    val terbilangOtomatis = remember(jumlah) { TerbilangUtils.konversi(jumlah) }

    val isValid = tanggal.isNotBlank() &&
            jumlah > 0L &&
            saksi1.isNotBlank() &&
            saksi2.isNotBlank() &&
            saksi3.isNotBlank() &&
            saksi4.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (beritaAwal != null) "Edit Berita Acara" else "Buat Berita Acara Baru",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Nomor Surat
                OutlinedTextField(
                    value = nomorSurat,
                    onValueChange = { nomorSurat = it },
                    label = { Text("Nomor Dokumen Berita Acara") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Hari Selector
                Column {
                    Text(
                        text = "Hari Pelaksanaan",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("Jumat", "Sabtu", "Minggu", "Senin", "Selasa", "Rabu", "Kamis")) { pilihan ->
                            FilterChip(
                                selected = hari == pilihan,
                                onClick = { hari = pilihan },
                                label = { Text(pilihan) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Emas,
                                    selectedLabelColor = Hijau,
                                    containerColor = HijauKartu,
                                    labelColor = Krem
                                )
                            )
                        }
                    }
                }

                // Tanggal Field
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = tanggal,
                        onValueChange = { tanggal = it },
                        label = { Text("Tanggal *") },
                        placeholder = { Text("Pilih tanggal") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Pilih Tanggal"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Buka Kalender"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        isError = tanggal.isBlank()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }

                // Jumlah Infak (dengan Terbilang Otomatis)
                Column {
                    OutlinedTextField(
                        value = jumlahText,
                        onValueChange = { jumlahText = it.filter { c -> c.isDigit() } },
                        label = { Text("Jumlah Total Infak (Rp) *") },
                        placeholder = { Text("Contoh: 5500000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = jumlah <= 0L
                    )
                    if (jumlah > 0L) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Terbilang: $terbilangOtomatis",
                            color = Emas,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                HorizontalDivider()

                Text(
                    text = "Empat (4) Saksi & Tanda Tangan *",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // Saksi 1
                SaksiInputRow(
                    label = "Saksi 1 (Ketua DKM / Pengurus) *",
                    nama = saksi1,
                    onNamaChange = { saksi1 = it },
                    isSigned = saksi1Ttd.isNotBlank(),
                    onSignClick = { signatureIndexToEdit = 1 }
                )

                // Saksi 2
                SaksiInputRow(
                    label = "Saksi 2 (Bendahara / Kasir) *",
                    nama = saksi2,
                    onNamaChange = { saksi2 = it },
                    isSigned = saksi2Ttd.isNotBlank(),
                    onSignClick = { signatureIndexToEdit = 2 }
                )

                // Saksi 3
                SaksiInputRow(
                    label = "Saksi 3 (Penghitung 1 / Jamaah) *",
                    nama = saksi3,
                    onNamaChange = { saksi3 = it },
                    isSigned = saksi3Ttd.isNotBlank(),
                    onSignClick = { signatureIndexToEdit = 3 }
                )

                // Saksi 4
                SaksiInputRow(
                    label = "Saksi 4 (Penghitung 2 / Jamaah) *",
                    nama = saksi4,
                    onNamaChange = { saksi4 = it },
                    isSigned = saksi4Ttd.isNotBlank(),
                    onSignClick = { signatureIndexToEdit = 4 }
                )

                // Keterangan
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = { Text("Catatan Tambahan") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValid) {
                        onSimpan(
                            BeritaAcara(
                                id = beritaAwal?.id ?: UUID.randomUUID().toString(),
                                nomorSurat = nomorSurat.trim(),
                                hari = hari,
                                tanggal = tanggal.trim(),
                                jumlahInfak = jumlah,
                                terbilang = terbilangOtomatis,
                                saksi1 = saksi1.trim(),
                                saksi2 = saksi2.trim(),
                                saksi3 = saksi3.trim(),
                                saksi4 = saksi4.trim(),
                                saksi1Ttd = saksi1Ttd,
                                saksi2Ttd = saksi2Ttd,
                                saksi3Ttd = saksi3Ttd,
                                saksi4Ttd = saksi4Ttd,
                                keterangan = keterangan.trim()
                            )
                        )
                    }
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emas,
                    contentColor = Hijau
                )
            ) {
                Text("Simpan & Generasi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("id-ID"))
                            tanggal = sdf.format(Date(millis))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Signature Touch Pad Modal for Active Witness
    if (signatureIndexToEdit != null) {
        val idx = signatureIndexToEdit!!
        val targetName = when (idx) {
            1 -> saksi1
            2 -> saksi2
            3 -> saksi3
            else -> saksi4
        }
        val targetTtd = when (idx) {
            1 -> saksi1Ttd
            2 -> saksi2Ttd
            3 -> saksi3Ttd
            else -> saksi4Ttd
        }

        SignatureDialog(
            namaSaksi = if (targetName.isBlank()) "Saksi $idx" else targetName,
            saksiIndex = idx,
            ttdAwal = targetTtd,
            onDismiss = { signatureIndexToEdit = null },
            onSimpanTtd = { newTtd ->
                when (idx) {
                    1 -> saksi1Ttd = newTtd
                    2 -> saksi2Ttd = newTtd
                    3 -> saksi3Ttd = newTtd
                    else -> saksi4Ttd = newTtd
                }
                signatureIndexToEdit = null
            }
        )
    }
}

@Composable
private fun SaksiInputRow(
    label: String,
    nama: String,
    onNamaChange: (String) -> Unit,
    isSigned: Boolean,
    onSignClick: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text(label) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = onSignClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Tanda Tangan",
                        tint = if (isSigned) Hijau else Emas
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = nama.isBlank()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onSignClick) {
                Text(
                    text = if (isSigned) "✍ Ttd Terrekam (Ubah)" else "✍ Sentuh untuk Ttd Digital",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSigned) Hijau else Emas
                )
            }
        }
    }
}

@Preview
@Composable
fun BeritaAcaraFormDialogPreview() {
    BeritaAcaraFormDialog(
        beritaAwal = null,
        onDismiss = {},
        onSimpan = {}
    )
}
