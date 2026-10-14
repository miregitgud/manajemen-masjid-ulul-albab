package com.mesjidululalbab.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun KolomInput(
    label: String,
    nilai: String,
    modifier: Modifier = Modifier,
    berubah: (String) -> Unit
) {
    OutlinedTextField(
        value = nilai,
        onValueChange = berubah,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = label != "Keterangan"
    )
}

@Preview(showBackground = true)
@Composable
fun KolomInputPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        KolomInput(
            label = "Nama kegiatan *",
            nilai = "Kultum Subuh",
            berubah = {}
        )
        KolomInput(
            label = "Penceramah / petugas",
            nilai = "",
            berubah = {}
        )
    }
}
