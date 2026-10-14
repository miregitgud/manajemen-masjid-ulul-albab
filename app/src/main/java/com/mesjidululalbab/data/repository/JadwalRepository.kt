package com.mesjidululalbab.data.repository

import android.content.Context
import androidx.core.content.edit
import com.mesjidululalbab.data.model.Jadwal
import org.json.JSONArray
import org.json.JSONObject

class JadwalRepository(private val context: Context) {

    fun muatJadwal(): List<Jadwal> {
        val hasil = mutableListOf<Jadwal>()
        val json = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).getString(KEY_JADWAL, "[]") ?: "[]"

        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                hasil.add(
                    Jadwal(
                        kategori = o.optString("kategori"),
                        nama = o.optString("nama"),
                        tanggal = o.optString("tanggal"),
                        waktu = o.optString("waktu"),
                        tempat = o.optString("tempat"),
                        petugas = o.optString("petugas"),
                        keterangan = o.optString("keterangan")
                    )
                )
            }
        } catch (_: Exception) {
        }
        return hasil
    }

    fun simpanJadwal(data: List<Jadwal>) {
        val array = JSONArray()
        data.forEach { j ->
            array.put(JSONObject().apply {
                put("kategori", j.kategori)
                put("nama", j.nama)
                put("tanggal", j.tanggal)
                put("waktu", j.waktu)
                put("tempat", j.tempat)
                put("petugas", j.petugas)
                put("keterangan", j.keterangan)
            })
        }
        context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).edit { putString(KEY_JADWAL, array.toString()) }
    }

    companion object {
        private const val PREFS_NAME = "masjid_data"
        private const val KEY_JADWAL = "jadwal"
    }
}
