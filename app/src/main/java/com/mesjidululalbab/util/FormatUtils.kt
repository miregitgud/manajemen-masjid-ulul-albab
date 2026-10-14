package com.mesjidululalbab.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(jumlah: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(jumlah)
}
