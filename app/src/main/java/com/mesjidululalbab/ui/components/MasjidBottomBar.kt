package com.mesjidululalbab.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau
import com.mesjidululalbab.ui.theme.HijauKartu
import com.mesjidululalbab.ui.theme.Krem

data class NavItem(
    val id: String,
    val label: String,
    val ikon: String
)

val itemNavigasi = listOf(
    NavItem("Beranda", "Beranda", "🏠"),
    NavItem("Jadwal", "Jadwal", "📅"),
    NavItem("Infak", "Infak", "💰"),
    NavItem("Berita", "Berita", "📋")
)

@Composable
fun MasjidBottomBar(
    halamanAktif: String,
    onPilihHalaman: (String) -> Unit
) {
    NavigationBar(
        containerColor = HijauKartu,
        contentColor = Krem
    ) {
        itemNavigasi.forEach { item ->
            val terpilih = halamanAktif == item.id
            NavigationBarItem(
                selected = terpilih,
                onClick = { onPilihHalaman(item.id) },
                icon = {
                    Text(
                        text = item.ikon,
                        fontSize = 20.sp
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontWeight = if (terpilih) FontWeight.Bold else FontWeight.Normal,
                        color = if (terpilih) Emas else Krem
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Emas,
                    selectedTextColor = Emas,
                    unselectedIconColor = Krem,
                    unselectedTextColor = Krem,
                    indicatorColor = Hijau
                )
            )
        }
    }
}

@Preview
@Composable
fun MasjidBottomBarPreview() {
    MasjidBottomBar(
        halamanAktif = "Beranda",
        onPilihHalaman = {}
    )
}
