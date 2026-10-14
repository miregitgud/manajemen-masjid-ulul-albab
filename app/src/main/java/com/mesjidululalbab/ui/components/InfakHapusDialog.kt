package com.mesjidululalbab.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mesjidululalbab.ui.theme.MerahHapus

@Composable
fun InfakHapusDialog(
    onDismiss: () -> Unit,
    onKonfirmasiHapus: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Catatan Infak?") },
        text = { Text("Catatan infak ini akan dihapus permanen dari daftar.") },
        confirmButton = {
            TextButton(onClick = onKonfirmasiHapus) {
                Text("Hapus", color = MerahHapus)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Preview
@Composable
fun InfakHapusDialogPreview() {
    InfakHapusDialog(
        onDismiss = {},
        onKonfirmasiHapus = {}
    )
}
