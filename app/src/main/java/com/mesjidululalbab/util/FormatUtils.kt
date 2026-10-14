package com.mesjidululalbab.util

import com.mesjidululalbab.data.model.Jadwal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatRupiah(jumlah: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(jumlah)
}

fun hitungTanggalBerikutnya(jadwal: Jadwal): Date? {
    if (jadwal.tanggal.isBlank()) return null
    val calToday = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val sdfFull = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("id-ID"))

    return when (jadwal.kategori) {
        "Harian" -> {
            val date = try { sdfFull.parse(jadwal.tanggal) } catch (_: Exception) { null } ?: return null
            val cal = Calendar.getInstance().apply { time = date }
            if (cal.timeInMillis >= calToday.timeInMillis) cal.time else null
        }

        "Pekanan" -> {
            val targetDayOfWeek = when (jadwal.tanggal.trim().lowercase()) {
                "minggu", "setiap hari minggu" -> Calendar.SUNDAY
                "senin", "setiap hari senin" -> Calendar.MONDAY
                "selasa", "setiap hari selasa" -> Calendar.TUESDAY
                "rabu", "setiap hari rabu" -> Calendar.WEDNESDAY
                "kamis", "setiap hari kamis" -> Calendar.THURSDAY
                "jumat", "setiap hari jumat" -> Calendar.FRIDAY
                "sabtu", "setiap hari sabtu" -> Calendar.SATURDAY
                else -> {
                    val d = try { sdfFull.parse(jadwal.tanggal) } catch (_: Exception) { null }
                    if (d != null) {
                        val c = Calendar.getInstance().apply { time = d }
                        c.get(Calendar.DAY_OF_WEEK)
                    } else Calendar.FRIDAY
                }
            }

            val calTarget = Calendar.getInstance().apply {
                time = calToday.time
            }
            while (calTarget.get(Calendar.DAY_OF_WEEK) != targetDayOfWeek) {
                calTarget.add(Calendar.DAY_OF_YEAR, 1)
            }
            calTarget.time
        }

        "Bulanan" -> {
            val dayOfMonth = jadwal.tanggal.filter { it.isDigit() }.toIntOrNull() ?: 1
            val calTarget = Calendar.getInstance().apply {
                time = calToday.time
                val maxDay = getActualMaximum(Calendar.DAY_OF_MONTH)
                set(Calendar.DAY_OF_MONTH, dayOfMonth.coerceIn(1, maxDay))
            }
            if (calTarget.timeInMillis < calToday.timeInMillis) {
                calTarget.add(Calendar.MONTH, 1)
                val maxDay = calTarget.getActualMaximum(Calendar.DAY_OF_MONTH)
                calTarget.set(Calendar.DAY_OF_MONTH, dayOfMonth.coerceIn(1, maxDay))
            }
            calTarget.time
        }

        "Tahunan" -> {
            val namaBulanMap = mapOf(
                "januari" to 0, "februari" to 1, "maret" to 2, "april" to 3,
                "mei" to 4, "juni" to 5, "juli" to 6, "agustus" to 7,
                "september" to 8, "oktober" to 9, "november" to 10, "desember" to 11
            )

            var day = 1
            var month = 0

            val parts = jadwal.tanggal.split(" ", "/", "-")
            if (parts.size >= 2) {
                day = parts[0].filter { it.isDigit() }.toIntOrNull() ?: 1
                val monthPart = parts[1].trim().lowercase()
                month = monthPart.filter { it.isDigit() }.toIntOrNull()?.let { it - 1 }
                    ?: namaBulanMap[monthPart] ?: 0
            } else {
                day = jadwal.tanggal.filter { it.isDigit() }.toIntOrNull() ?: 1
            }

            val calTarget = Calendar.getInstance().apply {
                time = calToday.time
                set(Calendar.MONTH, month.coerceIn(0, 11))
                val maxDay = getActualMaximum(Calendar.DAY_OF_MONTH)
                set(Calendar.DAY_OF_MONTH, day.coerceIn(1, maxDay))
            }

            if (calTarget.timeInMillis < calToday.timeInMillis) {
                calTarget.add(Calendar.YEAR, 1)
                calTarget.set(Calendar.MONTH, month.coerceIn(0, 11))
                val maxDay = calTarget.getActualMaximum(Calendar.DAY_OF_MONTH)
                calTarget.set(Calendar.DAY_OF_MONTH, day.coerceIn(1, maxDay))
            }
            calTarget.time
        }

        else -> {
            val date = try { sdfFull.parse(jadwal.tanggal) } catch (_: Exception) { null } ?: return null
            val cal = Calendar.getInstance().apply { time = date }
            if (cal.timeInMillis >= calToday.timeInMillis) cal.time else null
        }
    }
}

fun hitungTanggalBerikutnyaFormatted(jadwal: Jadwal): String {
    val nextDate = hitungTanggalBerikutnya(jadwal) ?: return jadwal.tanggal
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("id-ID"))
    return sdf.format(nextDate)
}

fun isJadwalMendatang(jadwal: Jadwal): Boolean {
    return hitungTanggalBerikutnya(jadwal) != null
}
