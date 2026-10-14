package com.mesjidululalbab.util

import java.util.Calendar
import java.util.Locale
import kotlin.math.*

data class JadwalShalatItem(
    val nama: String,
    val waktu: String,
    val isNext: Boolean = false
)

object PrayerTimeUtils {

    // Jakarta Coordinates (Kemenag RI reference default)
    private const val DEFAULT_LATITUDE = -6.2088
    private const val DEFAULT_LONGITUDE = 106.8456
    private const val TIMEZONE = 7.0

    fun getJadwalShalatHariIni(
        latitude: Double = DEFAULT_LATITUDE,
        longitude: Double = DEFAULT_LONGITUDE,
        calendar: Calendar = Calendar.getInstance()
    ): List<JadwalShalatItem> {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        // Declination of the Sun (approx)
        val d = 23.45 * sin(Math.toRadians(360.0 / 365.0 * (dayOfYear - 81)))
        val eqt = 9.87 * sin(Math.toRadians(2.0 * (360.0 / 365.0 * (dayOfYear - 81)))) -
                7.53 * cos(Math.toRadians(360.0 / 365.0 * (dayOfYear - 81))) -
                1.5 * sin(Math.toRadians(360.0 / 365.0 * (dayOfYear - 81)))

        // Noon (Dzuhur)
        val dzuhurDecimal = 12.0 + (TIMEZONE - longitude / 15.0) - (eqt / 60.0) + (2.0 / 60.0) // 2 min ihtiyat

        // Subuh (Kemenag: 20 degrees below horizon)
        val subuhAngle = 20.0
        val subuhHourAngle = calculateHourAngle(-subuhAngle, latitude, d)
        val subuhDecimal = dzuhurDecimal - (subuhHourAngle / 15.0)

        // Syuruq (Sunrise: 0.833 degrees below horizon)
        val syuruqHourAngle = calculateHourAngle(-0.833, latitude, d)
        val syuruqDecimal = dzuhurDecimal - (syuruqHourAngle / 15.0)

        // Ashar (Kemenag: Shadow = 1 + tan(|lat - dec|))
        val latRad = Math.toRadians(latitude)
        val decRad = Math.toRadians(d)
        val asharAlt = Math.toDegrees(atan(1.0 / (1.0 + tan(abs(latRad - decRad)))))
        val asharHourAngle = calculateHourAngle(asharAlt, latitude, d)
        val asharDecimal = dzuhurDecimal + (asharHourAngle / 15.0)

        // Maghrib (Sunset: 0.833 degrees)
        val maghribDecimal = dzuhurDecimal + (syuruqHourAngle / 15.0)

        // Isya (Kemenag: 18 degrees below horizon)
        val isyaAngle = 18.0
        val isyaHourAngle = calculateHourAngle(-isyaAngle, latitude, d)
        val isyaDecimal = dzuhurDecimal + (isyaHourAngle / 15.0)

        val listRaw = listOf(
            "Subuh" to formatTime(subuhDecimal),
            "Syuruq" to formatTime(syuruqDecimal),
            "Dzuhur" to formatTime(dzuhurDecimal),
            "Ashar" to formatTime(asharDecimal),
            "Maghrib" to formatTime(maghribDecimal),
            "Isya" to formatTime(isyaDecimal)
        )

        val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        var nextMarked = false

        return listRaw.map { (nama, waktuStr) ->
            val parts = waktuStr.split(".")
            val minutes = (parts.getOrNull(0)?.toIntOrNull() ?: 0) * 60 + (parts.getOrNull(1)?.toIntOrNull() ?: 0)
            val isNext = !nextMarked && minutes > currentMinutes
            if (isNext) nextMarked = true
            JadwalShalatItem(nama, waktuStr, isNext)
        }
    }

    private fun calculateHourAngle(altitude: Double, latitude: Double, declination: Double): Double {
        val altRad = Math.toRadians(altitude)
        val latRad = Math.toRadians(latitude)
        val decRad = Math.toRadians(declination)

        val cosHA = (sin(altRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
        val clamped = cosHA.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clamped))
    }

    private fun formatTime(decimalHours: Double): String {
        var h = decimalHours.toInt()
        var m = round((decimalHours - h) * 60.0).toInt()
        if (m >= 60) {
            h += 1
            m -= 60
        }
        return String.format(Locale.US, "%02d.%02d", h % 24, m)
    }
}
