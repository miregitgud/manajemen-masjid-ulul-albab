package com.mesjidululalbab.data.repository

import android.content.Context
import androidx.core.content.edit
import com.mesjidululalbab.data.model.BeritaAcara
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class BeritaAcaraRepository(private val context: Context) {

    fun muatBeritaAcara(): List<BeritaAcara> {
        val hasil = mutableListOf<BeritaAcara>()
        val json = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).getString(KEY_BERITA, "[]") ?: "[]"

        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                hasil.add(
                    BeritaAcara(
                        id = o.optString("id", UUID.randomUUID().toString()),
                        nomorSurat = o.optString("nomorSurat"),
                        hari = o.optString("hari", "Jumat"),
                        tanggal = o.optString("tanggal"),
                        jumlahInfak = o.optLong("jumlahInfak", 0L),
                        terbilang = o.optString("terbilang"),
                        saksi1 = o.optString("saksi1"),
                        saksi2 = o.optString("saksi2"),
                        saksi3 = o.optString("saksi3"),
                        saksi4 = o.optString("saksi4"),
                        saksi1Ttd = o.optString("saksi1Ttd"),
                        saksi2Ttd = o.optString("saksi2Ttd"),
                        saksi3Ttd = o.optString("saksi3Ttd"),
                        saksi4Ttd = o.optString("saksi4Ttd"),
                        keterangan = o.optString("keterangan")
                    )
                )
            }
        } catch (_: Exception) {
        }
        return hasil
    }

    fun simpanBeritaAcara(data: List<BeritaAcara>) {
        val array = JSONArray()
        data.forEach { item ->
            array.put(JSONObject().apply {
                put("id", item.id)
                put("nomorSurat", item.nomorSurat)
                put("hari", item.hari)
                put("tanggal", item.tanggal)
                put("jumlahInfak", item.jumlahInfak)
                put("terbilang", item.terbilang)
                put("saksi1", item.saksi1)
                put("saksi2", item.saksi2)
                put("saksi3", item.saksi3)
                put("saksi4", item.saksi4)
                put("saksi1Ttd", item.saksi1Ttd)
                put("saksi2Ttd", item.saksi2Ttd)
                put("saksi3Ttd", item.saksi3Ttd)
                put("saksi4Ttd", item.saksi4Ttd)
                put("keterangan", item.keterangan)
            })
        }
        context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).edit { putString(KEY_BERITA, array.toString()) }
    }

    companion object {
        private const val PREFS_NAME = "masjid_data"
        private const val KEY_BERITA = "berita_acara_jumat"
    }
}
