package com.mesjidululalbab.data.repository

import android.content.Context
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.data.model.InfakJumat
import com.mesjidululalbab.data.model.Jadwal
import org.json.JSONArray
import org.json.JSONObject

object BackupRepository {

    fun exportBackupJson(context: Context): String {
        val jadwalRepo = JadwalRepository(context)
        val infakRepo = InfakRepository(context)
        val beritaRepo = BeritaAcaraRepository(context)

        val root = JSONObject()

        // Jadwal Array
        val jadwalArray = JSONArray()
        jadwalRepo.muatJadwal().forEach { j ->
            jadwalArray.put(JSONObject().apply {
                put("kategori", j.kategori)
                put("nama", j.nama)
                put("tanggal", j.tanggal)
                put("waktu", j.waktu)
                put("tempat", j.tempat)
                put("petugas", j.petugas)
                put("keterangan", j.keterangan)
            })
        }

        // Infak Array
        val infakArray = JSONArray()
        infakRepo.muatInfak().forEach { i ->
            infakArray.put(JSONObject().apply {
                put("id", i.id)
                put("tanggal", i.tanggal)
                put("kategori", i.kategori)
                put("jumlahPemasukan", i.jumlahPemasukan)
                put("jumlahPengeluaran", i.jumlahPengeluaran)
                put("petugasPenghitung", i.petugasPenghitung)
                put("keterangan", i.keterangan)
            })
        }

        // Berita Array
        val beritaArray = JSONArray()
        beritaRepo.muatBeritaAcara().forEach { b ->
            beritaArray.put(JSONObject().apply {
                put("id", b.id)
                put("nomorSurat", b.nomorSurat)
                put("hari", b.hari)
                put("tanggal", b.tanggal)
                put("jumlahInfak", b.jumlahInfak)
                put("terbilang", b.terbilang)
                put("saksi1", b.saksi1)
                put("saksi2", b.saksi2)
                put("saksi3", b.saksi3)
                put("saksi4", b.saksi4)
                put("saksi1Ttd", b.saksi1Ttd)
                put("saksi2Ttd", b.saksi2Ttd)
                put("saksi3Ttd", b.saksi3Ttd)
                put("saksi4Ttd", b.saksi4Ttd)
                put("keterangan", b.keterangan)
            })
        }

        root.put("jadwal", jadwalArray)
        root.put("infak", infakArray)
        root.put("berita", beritaArray)

        return root.toString(2)
    }

    fun importBackupJson(
        context: Context,
        jsonStr: String
    ): Boolean {
        return try {
            val root = JSONObject(jsonStr)

            val jadwalRepo = JadwalRepository(context)
            val infakRepo = InfakRepository(context)
            val beritaRepo = BeritaAcaraRepository(context)

            if (root.has("jadwal")) {
                val array = root.getJSONArray("jadwal")
                val list = mutableListOf<Jadwal>()
                for (i in 0 until array.length()) {
                    val o = array.getJSONObject(i)
                    list.add(
                        Jadwal(
                            o.optString("kategori"),
                            o.optString("nama"),
                            o.optString("tanggal"),
                            o.optString("waktu"),
                            o.optString("tempat"),
                            o.optString("petugas"),
                            o.optString("keterangan")
                        )
                    )
                }
                jadwalRepo.simpanJadwal(list)
            }

            if (root.has("infak")) {
                val array = root.getJSONArray("infak")
                val list = mutableListOf<InfakJumat>()
                for (i in 0 until array.length()) {
                    val o = array.getJSONObject(i)
                    list.add(
                        InfakJumat(
                            o.optString("id"),
                            o.optString("tanggal"),
                            o.optString("kategori"),
                            o.optLong("jumlahPemasukan"),
                            o.optLong("jumlahPengeluaran"),
                            o.optString("petugasPenghitung"),
                            o.optString("keterangan")
                        )
                    )
                }
                infakRepo.simpanInfak(list)
            }

            if (root.has("berita")) {
                val array = root.getJSONArray("berita")
                val list = mutableListOf<BeritaAcara>()
                for (i in 0 until array.length()) {
                    val o = array.getJSONObject(i)
                    list.add(
                        BeritaAcara(
                            o.optString("id"),
                            o.optString("nomorSurat"),
                            o.optString("hari"),
                            o.optString("tanggal"),
                            o.optLong("jumlahInfak"),
                            o.optString("terbilang"),
                            o.optString("saksi1"),
                            o.optString("saksi2"),
                            o.optString("saksi3"),
                            o.optString("saksi4"),
                            o.optString("saksi1Ttd"),
                            o.optString("saksi2Ttd"),
                            o.optString("saksi3Ttd"),
                            o.optString("saksi4Ttd"),
                            o.optString("keterangan")
                        )
                    )
                }
                beritaRepo.simpanBeritaAcara(list)
            }

            true
        } catch (_: Exception) {
            false
        }
    }
}
