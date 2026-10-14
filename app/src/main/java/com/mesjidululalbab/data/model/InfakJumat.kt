package com.mesjidululalbab.data.model

data class InfakJumat(
    val id: String,
    val tanggal: String,
    val kategori: String,
    val jumlahPemasukan: Long,
    val jumlahPengeluaran: Long = 0L,
    val petugasPenghitung: String,
    val keterangan: String
) {
    val saldo: Long get() = jumlahPemasukan - jumlahPengeluaran
}
