package com.mesjidululalbab.util

object TerbilangUtils {
    private val satuan = arrayOf(
        "", "Satu", "Dua", "Tiga", "Empat", "Lima",
        "Enam", "Tujuh", "Delapan", "Sembilan", "Sepuluh", "Sebelas"
    )

    fun konversi(n: Long): String {
        if (n == 0L) return "Nol Rupiah"
        val kalimat = eja(n).trim()
        return "$kalimat Rupiah"
    }

    private fun eja(n: Long): String {
        return when {
            n < 12 -> satuan[n.toInt()]
            n < 20 -> eja(n - 10) + " Belas"
            n < 100 -> eja(n / 10) + " Puluh " + eja(n % 10)
            n < 200 -> "Seratus " + eja(n % 100)
            n < 1000 -> eja(n / 100) + " Ratus " + eja(n % 100)
            n < 2000 -> "Seribu " + eja(n % 1000)
            n < 1000000 -> eja(n / 1000) + " Ribu " + eja(n % 1000)
            n < 1000000000 -> eja(n / 1000000) + " Juta " + eja(n % 1000000)
            n < 1000000000000 -> eja(n / 1000000000) + " Miliar " + eja(n % 1000000000)
            else -> eja(n / 1000000000000) + " Triliun " + eja(n % 1000000000000)
        }.replace("\\s+".toRegex(), " ")
    }
}
