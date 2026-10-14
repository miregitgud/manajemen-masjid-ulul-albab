package com.mesjidululalbab.util

import android.content.Context
import android.content.Intent
import com.mesjidululalbab.data.model.BeritaAcara

object ShareUtils {

    fun bagikanBeritaAcara(context: Context, berita: BeritaAcara) {
        val text = """
            *BERITA ACARA PERHITUNGAN INFAK JUMAT*
            *MASJID ULUL ALBAB - PT. REKAYASA INDUSTRI*
            ------------------------------------------
            Nomor: ${berita.nomorSurat}
            Hari/Tanggal: ${berita.hari}, ${berita.tanggal}

            *Jumlah Total Infak:*
            ${formatRupiah(berita.jumlahInfak)}
            (_${berita.terbilang}_)

            *Empat (4) Saksi Penandatangan:*
            1. ${berita.saksi1}
            2. ${berita.saksi2}
            3. ${berita.saksi3}
            4. ${berita.saksi4}

            ${if (berita.keterangan.isNotBlank()) "Catatan: ${berita.keterangan}" else ""}
            ------------------------------------------
            _Dokumen Resmi DKM Masjid Ulul Albab_
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Berita Acara Infak - ${berita.nomorSurat}")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Bagikan Berita Acara via"))
    }

    fun bagikanLaporanInfak(context: Context, totalPemasukan: Long, totalPengeluaran: Long) {
        val saldo = totalPemasukan - totalPengeluaran
        val text = """
            *LAPORAN KAS INFAK JUMAT*
            *MASJID ULUL ALBAB - PT. REKAYASA INDUSTRI*
            ------------------------------------------
            *Total Pemasukan:* ${formatRupiah(totalPemasukan)}
            *Total Pengeluaran:* ${formatRupiah(totalPengeluaran)}
            ------------------------------------------
            *SALDO AKHIR KAS:* ${formatRupiah(saldo)}
            ------------------------------------------
            _Laporan Kas DKM Masjid Ulul Albab_
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Laporan Kas Infak Jumat")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Bagikan Laporan Infak via"))
    }
}
