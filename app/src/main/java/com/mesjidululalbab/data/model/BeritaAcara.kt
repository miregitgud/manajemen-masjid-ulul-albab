package com.mesjidululalbab.data.model

data class BeritaAcara(
    val id: String,
    val nomorSurat: String,
    val hari: String,
    val tanggal: String,
    val jumlahInfak: Long,
    val terbilang: String,
    val saksi1: String,
    val saksi2: String,
    val saksi3: String,
    val saksi4: String,
    val keterangan: String = ""
)
