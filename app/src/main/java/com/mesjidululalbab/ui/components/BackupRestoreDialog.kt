package com.mesjidululalbab.ui.components

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.repository.BackupRepository
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau

@Composable
fun BackupRestoreDialog(
    onDismiss: () -> Unit,
    onDataRestored: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) }
    var importText by remember { mutableStateOf("") }
    var importStatusMessage by remember { mutableStateOf("") }

    val backupJson = remember { BackupRepository.exportBackupJson(context) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Hijau,
                    contentColor = Emas
                )
            ) {
                Text("Tutup")
            }
        },
        title = {
            Text("Backup & Restore Data Masjid", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
            ) {
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Export Backup") }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("Import / Restore") }
                    )
                }

                Spacer(Modifier.height(12.dp))

                if (activeTab == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Ekspor seluruh data Jadwal, Infak, dan Berita Acara ke format JSON:",
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = backupJson,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Masjid Backup JSON", backupJson)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Teks backup berhasil disalin!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Emas, contentColor = Hijau),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📋 Salin Teks Backup")
                        }

                        Spacer(Modifier.height(6.dp))

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Backup Data Masjid Ulul Albab")
                                    putExtra(Intent.EXTRA_TEXT, backupJson)
                                }
                                context.startActivity(Intent.createChooser(intent, "Bagikan File Backup via"))
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📤 Bagikan Backup Data")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Tempel (paste) kode JSON backup data yang ingin dipulihkan:",
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = importText,
                            onValueChange = { importText = it },
                            placeholder = { Text("Tempel data JSON di sini...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )

                        if (importStatusMessage.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = importStatusMessage,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (importStatusMessage.contains("berhasil", ignoreCase = true)) Hijau else MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (importText.isNotBlank()) {
                                    val success = BackupRepository.importBackupJson(context, importText)
                                    if (success) {
                                        importStatusMessage = "Data berhasil dipulihkan!"
                                        Toast.makeText(context, "Data berhasil dipulihkan!", Toast.LENGTH_SHORT).show()
                                        onDataRestored()
                                    } else {
                                        importStatusMessage = "Gagal memproses data JSON. Periksa format teks."
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Emas, contentColor = Hijau),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📥 Pulihkan Data (Restore)")
                        }
                    }
                }
            }
        }
    )
}
