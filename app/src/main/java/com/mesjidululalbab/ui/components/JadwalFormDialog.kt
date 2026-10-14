package com.mesjidululalbab.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.model.Jadwal
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.Krem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
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

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val isFormValid = nama.isNotBlank() && tanggal.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (jadwalAwal != null) "Edit Jadwal Kegiatan" else "Tambah Jadwal Kegiatan",
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
                // Kategori Selector
                Column {
                    Text(
                        text = "Kategori Agenda",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("Harian", "Pekanan", "Bulanan", "Tahunan")) { pilihan ->
                            FilterChip(
                                selected = kategori == pilihan,
                                onClick = {
                                    if (kategori != pilihan) {
                                        kategori = pilihan
                                        tanggal = when (pilihan) {
                                            "Pekanan" -> "Jumat"
                                            "Bulanan" -> "Tanggal 15"
                                            "Tahunan" -> "15 Oktober"
                                            else -> ""
                                        }
                                    }
                                },
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

                // Nama Kegiatan
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Kegiatan *") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nama.isBlank()
                )

                // Category-dependent Date/Frequency Selector
                when (kategori) {
                    "Harian" -> {
                        // Full DatePicker Trigger
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = tanggal,
                                onValueChange = { tanggal = it },
                                label = { Text("Tanggal Kegiatan *") },
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
                    }

                    "Pekanan" -> {
                        Column {
                            Text(
                                text = "Hari Rutin Pekanan *",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            val daftarHari = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(daftarHari) { hariPilihan ->
                                    FilterChip(
                                        selected = tanggal.equals(hariPilihan, ignoreCase = true) || tanggal == "Setiap Hari $hariPilihan",
                                        onClick = { tanggal = hariPilihan },
                                        label = { Text(hariPilihan) },
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
                    }

                    "Bulanan" -> {
                        Column {
                            Text(
                                text = "Tanggal Rutin Tiap Bulan (1 - 31) *",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            val currentSelectedDay = tanggal.filter { it.isDigit() }.toIntOrNull() ?: 15
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items((1..31).toList()) { tglNum ->
                                    FilterChip(
                                        selected = currentSelectedDay == tglNum,
                                        onClick = { tanggal = "Tanggal $tglNum" },
                                        label = { Text("Tgl $tglNum") },
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
                    }

                    "Tahunan" -> {
                        val daftarBulan = listOf(
                            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                        )
                        var selectedDay by remember(tanggal) {
                            mutableIntStateOf(tanggal.split(" ").firstOrNull()?.filter { it.isDigit() }?.toIntOrNull() ?: 15)
                        }
                        var selectedMonth by remember(tanggal) {
                            mutableStateOf(
                                daftarBulan.firstOrNull { b -> tanggal.contains(b, ignoreCase = true) } ?: "Oktober"
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Tanggal & Bulan Rutin Tiap Tahun *",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text("Pilih Tanggal:", fontSize = 11.sp, color = Krem)
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items((1..31).toList()) { tglNum ->
                                    FilterChip(
                                        selected = selectedDay == tglNum,
                                        onClick = {
                                            selectedDay = tglNum
                                            tanggal = "$selectedDay $selectedMonth"
                                        },
                                        label = { Text("$tglNum") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Emas,
                                            selectedLabelColor = Hijau,
                                            containerColor = HijauKartu,
                                            labelColor = Krem
                                        )
                                    )
                                }
                            }

                            Text("Pilih Bulan:", fontSize = 11.sp, color = Krem)
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(daftarBulan) { blnName ->
                                    FilterChip(
                                        selected = selectedMonth.equals(blnName, ignoreCase = true),
                                        onClick = {
                                            selectedMonth = blnName
                                            tanggal = "$selectedDay $selectedMonth"
                                        },
                                        label = { Text(blnName) },
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
                    }
                }

                // Waktu Field with TimePicker Trigger
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = waktu,
                        onValueChange = { waktu = it },
                        label = { Text("Waktu") },
                        placeholder = { Text("Pilih waktu") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Pilih Waktu"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Buka Jam"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )
                    // Clickable overlay for entire field
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showTimePicker = true }
                    )
                }

                // Tempat
                OutlinedTextField(
                    value = tempat,
                    onValueChange = { tempat = it },
                    label = { Text("Tempat") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Penceramah / Petugas
                OutlinedTextField(
                    value = petugas,
                    onValueChange = { petugas = it },
                    label = { Text("Penceramah / Petugas") },
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
                    label = { Text("Keterangan Tambahan") },
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
                    if (isFormValid) {
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
                },
                enabled = isFormValid,
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

    // DatePicker Dialog for Harian
    if (showDatePicker && kategori == "Harian") {
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

    // TimePicker Dialog
    if (showTimePicker) {
        val cal = Calendar.getInstance()
        val timePickerState = rememberTimePickerState(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val jam = timePickerState.hour.toString().padStart(2, '0')
                        val menit = timePickerState.minute.toString().padStart(2, '0')
                        waktu = "$jam.$menit WIB"
                        showTimePicker = false
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Batal")
                }
            },
            title = {
                Text("Pilih Waktu Agenda", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }
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
