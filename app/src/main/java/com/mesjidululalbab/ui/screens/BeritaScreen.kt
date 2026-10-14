package com.mesjidululalbab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.Krem

@Composable
fun BeritaScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Formulir Berita Acara dengan empat saksi akan kita lengkapi berikutnya.",
            color = Krem,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BeritaScreenPreview() {
    Surface(color = Hijau) {
        BeritaScreen()
    }
}
