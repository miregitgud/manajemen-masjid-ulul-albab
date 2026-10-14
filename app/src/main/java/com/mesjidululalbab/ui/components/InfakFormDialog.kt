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
    var jenisTransaksi by remember {
        mutableStateOf(if ((infakAwal?.jumlahPengeluaran ?: 0L) > (infakAwal?.jumlahPemasukan ?: 0L)) "Pengeluaran" else "Pemasukan")
    }

    var kategori by remember {
        mutableStateOf(infakAwal?.kategori ?: if (jenisTransaksi == "Pemasukan") "Kotak Infak Jumat" else "Operasional Masjid")
    }
    var tanggal by remember { mutableStateOf(infakAwal?.tanggal ?: "") }
    var jumlahText by remember {
        mutableStateOf(
            if (jenisTransaksi == "Pemasukan")
                (if ((infakAwal?.jumlahPemasukan ?: 0L) > 0L) infakAwal?.jumlahPemasukan?.toString() else "") ?: ""
            else
                (if ((infakAwal?.jumlahPengeluaran ?: 0L) > 0L) infakAwal?.jumlahPengeluaran?.toString() else "") ?: ""
        )
    }
    var petugas by remember { mutableStateOf(infakAwal?.petugasPenghitung ?: "") }
    var keterangan by remember { mutableStateOf(infakAwal?.keterangan ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }

    val nominal = jumlahText.toLongOrNull() ?: 0L
    val isValid = tanggal.isNotBlank() && nominal > 0L

    val opsiPemasukan = listOf("Kotak Infak Jumat", "Transfer Bank", "QRIS", "Donatur Tetap")
    val opsiPengeluaran = listOf("Operasional Masjid", "Honor Khotib / Ustadz", "Kebersihan & Sanitasi", "Listrik & Air", "Pemeliharaan Bangunan")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (infakAwal != null) "Edit Catatan Kas" else "Tambah Catatan Kas Infak",
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
                // Jenis Transaksi Selector
                Column {
                    Text(
                        text = "Jenis Transaksi *",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = jenisTransaksi == "Pemasukan",
                            onClick = {
                                jenisTransaksi = "Pemasukan"
                                kategori = "Kotak Infak Jumat"
                            },
                            label = { Text("🟢 Pemasukan") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Emas,
                                selectedLabelColor = Hijau,
                                containerColor = HijauKartu,
                                labelColor = Krem
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = jenisTransaksi == "Pengeluaran",
                            onClick = {
                                jenisTransaksi = "Pengeluaran"
                                kategori = "Operasional Masjid"
                            },
                            label = { Text("🔴 Pengeluaran") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.error,
                                selectedLabelColor = MaterialTheme.colorScheme.onError,
                                containerColor = HijauKartu,
                                labelColor = Krem
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Kategori Selector
                Column {
                    Text(
                        text = "Kategori $jenisTransaksi",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(if (jenisTransaksi == "Pemasukan") opsiPemasukan else opsiPengeluaran) { pilihan ->
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

                // Jumlah Nominal
                OutlinedTextField(
                    value = jumlahText,
                    onValueChange = { jumlahText = it.filter { c -> c.isDigit() } },
                    label = { Text("Jumlah $jenisTransaksi (Rp) *") },
                    placeholder = { Text("Contoh: 500000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nominal <= 0L
                )

                // Petugas / Saksi
                OutlinedTextField(
                    value = petugas,
                    onValueChange = { petugas = it },
                    label = { Text("Petugas / Saksi Penanggung Jawab") },
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
                    label = { Text("Catatan / Keterangan") },
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
                                jumlahPemasukan = if (jenisTransaksi == "Pemasukan") nominal else 0L,
                                jumlahPengeluaran = if (jenisTransaksi == "Pengeluaran") nominal else 0L,
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
                Text("Simpan Transaksi")
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
