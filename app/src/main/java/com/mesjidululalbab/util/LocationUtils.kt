package com.mesjidululalbab.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.edit
import java.util.Locale

data class LokasiShalat(
    val namaKota: String,
    val latitude: Double,
    val longitude: Double
)

val daftarKotaPopuler = listOf(
    LokasiShalat("Jakarta Selatan", -6.2088, 106.8456),
    LokasiShalat("Jakarta Pusat", -6.1818, 106.8223),
    LokasiShalat("Bandung", -6.9175, 107.6191),
    LokasiShalat("Surabaya", -7.2575, 112.7521),
    LokasiShalat("Semarang", -6.9667, 110.4167),
    LokasiShalat("Yogyakarta", -7.7956, 110.3695),
    LokasiShalat("Medan", 3.5952, 98.6722),
    LokasiShalat("Makassar", -5.1477, 119.4327),
    LokasiShalat("Palembang", -2.9761, 104.7754)
)

object LocationRepository {
    private const val PREFS_NAME = "masjid_data"
    private const val KEY_NAMA_KOTA = "lokasi_nama_kota"
    private const val KEY_LAT = "lokasi_lat"
    private const val KEY_LNG = "lokasi_lng"

    fun simpanLokasi(context: Context, lokasi: LokasiShalat) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString(KEY_NAMA_KOTA, lokasi.namaKota)
            putFloat(KEY_LAT, lokasi.latitude.toFloat())
            putFloat(KEY_LNG, lokasi.longitude.toFloat())
        }
    }

    fun muatLokasi(context: Context): LokasiShalat {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val nama = prefs.getString(KEY_NAMA_KOTA, "Jakarta Selatan") ?: "Jakarta Selatan"
        val lat = prefs.getFloat(KEY_LAT, -6.2088f).toDouble()
        val lng = prefs.getFloat(KEY_LNG, 106.8456f).toDouble()
        return LokasiShalat(nama, lat, lng)
    }
}

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun deteksiLokasiGPS(context: Context): LokasiShalat? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return null

        var bestLocation: Location? = null

        try {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                val l = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < (bestLocation?.accuracy ?: Float.MAX_VALUE)) {
                    bestLocation = l
                }
            }
        } catch (_: SecurityException) {
            return null
        }

        val loc = bestLocation ?: return null
        val lat = loc.latitude
        val lng = loc.longitude

        var nama = "Lokasi GPS (${String.format(Locale.US, "%.2f, %.2f", lat, lng)})"

        try {
            val geocoder = Geocoder(context, Locale.forLanguageTag("id-ID"))
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val subAdmin = addr.subAdminArea ?: addr.locality ?: addr.adminArea
                if (!subAdmin.isNullOrBlank()) {
                    nama = subAdmin
                }
            }
        } catch (_: Exception) {
        }

        return LokasiShalat(nama, lat, lng)
    }
}
