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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var keterangan by remember { mutableStateOf(beritaAwal?.keterangan ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }

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
                    text = "Empat (4) Saksi Penandatangan *",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    value = saksi1,
                    onValueChange = { saksi1 = it },
                    label = { Text("Saksi 1 (Ketua DKM / Pengurus) *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = saksi1.isBlank()
                )

                OutlinedTextField(
                    value = saksi2,
                    onValueChange = { saksi2 = it },
                    label = { Text("Saksi 2 (Bendahara / Kasir) *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = saksi2.isBlank()
                )

                OutlinedTextField(
                    value = saksi3,
                    onValueChange = { saksi3 = it },
                    label = { Text("Saksi 3 (Penghitung 1 / Jamaah) *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = saksi3.isBlank()
                )

                OutlinedTextField(
                    value = saksi4,
                    onValueChange = { saksi4 = it },
                    label = { Text("Saksi 4 (Penghitung 2 / Jamaah) *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = saksi4.isBlank()
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
