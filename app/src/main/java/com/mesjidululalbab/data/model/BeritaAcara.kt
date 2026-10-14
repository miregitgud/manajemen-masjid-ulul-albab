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
    val saksi1Ttd: String = "",
    val saksi2Ttd: String = "",
    val saksi3Ttd: String = "",
    val saksi4Ttd: String = "",
    val keterangan: String = ""
)
