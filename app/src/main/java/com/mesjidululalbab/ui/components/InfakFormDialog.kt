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
import com.mesjidululalbab.data.model.InfakJumat
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.Krem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfakFormDialog(
    infakAwal: InfakJumat?,
    onDismiss: () -> Unit,
    onSimpan: (InfakJumat) -> Unit
) {
    var kategori by remember { mutableStateOf(infakAwal?.kategori ?: "Kotak Infak Jumat") }
    var tanggal by remember { mutableStateOf(infakAwal?.tanggal ?: "") }
    var pemasukanText by remember { mutableStateOf(infakAwal?.jumlahPemasukan?.toString() ?: "") }
    var pengeluaranText by remember { mutableStateOf(infakAwal?.jumlahPengeluaran?.toString() ?: "0") }
    var petugas by remember { mutableStateOf(infakAwal?.petugasPenghitung ?: "") }
    var keterangan by remember { mutableStateOf(infakAwal?.keterangan ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }

    val pemasukan = pemasukanText.toLongOrNull() ?: 0L
    val pengeluaran = pengeluaranText.toLongOrNull() ?: 0L
    val isValid = tanggal.isNotBlank() && pemasukan > 0L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (infakAwal != null) "Edit Catatan Infak" else "Tambah Catatan Infak",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Kategori Selector
                Column {
                    Text(
                        text = "Kategori Sumber Infak",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("Kotak Infak Jumat", "Transfer Bank", "QRIS", "Donatur Tetap")) { pilihan ->
                            FilterChip(
                                selected = kategori == pilihan,
                                onClick = { kategori = pilihan },
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

                // Jumlah Pemasukan
                OutlinedTextField(
                    value = pemasukanText,
                    onValueChange = { pemasukanText = it.filter { c -> c.isDigit() } },
                    label = { Text("Jumlah Pemasukan (Rp) *") },
                    placeholder = { Text("Contoh: 5000000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = pemasukan <= 0L
                )

                // Jumlah Pengeluaran
                OutlinedTextField(
                    value = pengeluaranText,
                    onValueChange = { pengeluaranText = it.filter { c -> c.isDigit() } },
                    label = { Text("Pengeluaran Terkait (Rp)") },
                    placeholder = { Text("Contoh: 0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Petugas / Saksi
                OutlinedTextField(
                    value = petugas,
                    onValueChange = { petugas = it },
                    label = { Text("Petugas Penghitung / Saksi") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Keterangan
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = { Text("Keterangan") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                    },
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
                            InfakJumat(
                                id = infakAwal?.id ?: UUID.randomUUID().toString(),
                                tanggal = tanggal.trim(),
                                kategori = kategori,
                                jumlahPemasukan = pemasukan,
                                jumlahPengeluaran = pengeluaran,
                                petugasPenghitung = petugas.trim(),
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
                Text("Simpan")
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
fun InfakFormDialogPreview() {
    InfakFormDialog(
        infakAwal = null,
        onDismiss = {},
        onSimpan = {}
    )
}
