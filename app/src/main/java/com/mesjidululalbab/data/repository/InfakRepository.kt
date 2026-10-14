package com.mesjidululalbab.data.repository

import android.content.Context
import androidx.core.content.edit
import com.mesjidululalbab.data.model.InfakJumat
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class InfakRepository(private val context: Context) {

    fun muatInfak(): List<InfakJumat> {
        val hasil = mutableListOf<InfakJumat>()
        val json = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).getString(KEY_INFAK, "[]") ?: "[]"

        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                hasil.add(
                    InfakJumat(
                        id = o.optString("id", UUID.randomUUID().toString()),
                        tanggal = o.optString("tanggal"),
                        kategori = o.optString("kategori", "Kotak Infak Jumat"),
                        jumlahPemasukan = o.optLong("jumlahPemasukan", 0L),
                        jumlahPengeluaran = o.optLong("jumlahPengeluaran", 0L),
                        petugasPenghitung = o.optString("petugasPenghitung"),
                        keterangan = o.optString("keterangan")
                    )
                )
            }
        } catch (_: Exception) {
        }
        return hasil
    }

    fun simpanInfak(data: List<InfakJumat>) {
        val array = JSONArray()
        data.forEach { item ->
            array.put(JSONObject().apply {
                put("id", item.id)
                put("tanggal", item.tanggal)
                put("kategori", item.kategori)
                put("jumlahPemasukan", item.jumlahPemasukan)
                put("jumlahPengeluaran", item.jumlahPengeluaran)
                put("petugasPenghitung", item.petugasPenghitung)
                put("keterangan", item.keterangan)
            })
        }
        context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).edit { putString(KEY_INFAK, array.toString()) }
    }

    companion object {
        private const val PREFS_NAME = "masjid_data"
        private const val KEY_INFAK = "infak_jumat"
    }
}
