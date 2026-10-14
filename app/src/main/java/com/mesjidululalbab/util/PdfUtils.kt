package com.mesjidululalbab.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.ui.components.SignatureUtils
import java.io.File
import java.io.FileOutputStream

object PdfUtils {

    fun generateBeritaAcaraPdf(context: Context, berita: BeritaAcara): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paintText = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            isAntiAlias = true
        }

        val paintTitle = Paint().apply {
            color = Color.parseColor("#123D2B")
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val paintSubTitle = Paint().apply {
            color = Color.DKGRAY
            textSize = 10f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val paintHeaderDoc = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val paintBoxBg = Paint().apply {
            color = Color.parseColor("#F8F5EA")
            style = Paint.Style.FILL
        }

        val paintBoxBorder = Paint().apply {
            color = Color.parseColor("#123D2B")
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }

        val paintLine = Paint().apply {
            color = Color.parseColor("#123D2B")
            strokeWidth = 2f
        }

        val paintSignatureStroke = Paint().apply {
            color = Color.parseColor("#123D2B")
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }

        var startY = 40f
        val startX = 40f
        val contentWidth = 515f
        val centerX = 297.5f

        // Kop Surat Header
        canvas.drawText("MASJID ULUL ALBAB", centerX, startY, paintTitle)
        startY += 16f
        canvas.drawText("PT. REKAYASA INDUSTRI • JAKARTA SELATAN", centerX, startY, paintSubTitle)
        startY += 12f
        canvas.drawLine(startX, startY, startX + contentWidth, startY, paintLine)
        startY += 24f

        // Document Title
        canvas.drawText("BERITA ACARA PERHITUNGAN INFAK JUMAT", centerX, startY, paintHeaderDoc)
        startY += 16f
        paintSubTitle.textSize = 11f
        canvas.drawText("Nomor: ${berita.nomorSurat}", centerX, startY, paintSubTitle)
        startY += 30f

        // Body Statement Text
        val statement = "Pada hari ini ${berita.hari}, tanggal ${berita.tanggal}, telah dilaksanakan perhitungan penerimaan dana Infak Jumat pada Masjid Ulul Albab PT. Rekayasa Industri dengan hasil rincian sebagai berikut:"
        startY = drawWrappedText(canvas, statement, startX, startY, contentWidth, paintText, 16f)
        startY += 16f

        // Amount Box
        val boxHeight = 70f
        canvas.drawRect(startX, startY, startX + contentWidth, startY + boxHeight, paintBoxBg)
        canvas.drawRect(startX, startY, startX + contentWidth, startY + boxHeight, paintBoxBorder)

        val paintBoxTitle = Paint().apply {
            color = Color.GRAY
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val paintBoxAmount = Paint().apply {
            color = Color.parseColor("#123D2B")
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val paintBoxTerbilang = Paint().apply {
            color = Color.DKGRAY
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }

        canvas.drawText("JUMLAH TOTAL INFAK", centerX, startY + 20f, paintBoxTitle)
        canvas.drawText(formatRupiah(berita.jumlahInfak), centerX, startY + 42f, paintBoxAmount)
        canvas.drawText("Terbilang: ${berita.terbilang}", centerX, startY + 60f, paintBoxTerbilang)

        startY += boxHeight + 20f

        if (berita.keterangan.isNotBlank()) {
            startY = drawWrappedText(canvas, "Catatan Tambahan: ${berita.keterangan}", startX, startY, contentWidth, paintText, 16f)
            startY += 16f
        }

        // Witness Statement
        val witnessText = "Demikian Berita Acara ini dibuat secara sah dan disaksikan oleh empat (4) saksi penandatangan di bawah ini:"
        startY = drawWrappedText(canvas, witnessText, startX, startY, contentWidth, paintText, 16f)
        startY += 24f

        // 4 Witnesses Grid (2x2)
        val cardW = (contentWidth - 20f) / 2f
        val cardH = 90f

        drawSaksiPdfBlock(canvas, "Saksi I (Ketua DKM)", berita.saksi1, berita.saksi1Ttd, startX, startY, cardW, cardH, paintBoxBorder, paintSignatureStroke)
        drawSaksiPdfBlock(canvas, "Saksi II (Bendahara)", berita.saksi2, berita.saksi2Ttd, startX + cardW + 20f, startY, cardW, cardH, paintBoxBorder, paintSignatureStroke)

        startY += cardH + 16f

        drawSaksiPdfBlock(canvas, "Saksi III (Penghitung 1)", berita.saksi3, berita.saksi3Ttd, startX, startY, cardW, cardH, paintBoxBorder, paintSignatureStroke)
        drawSaksiPdfBlock(canvas, "Saksi IV (Penghitung 2)", berita.saksi4, berita.saksi4Ttd, startX + cardW + 20f, startY, cardW, cardH, paintBoxBorder, paintSignatureStroke)

        pdfDocument.finishPage(page)

        // Save PDF to cache file
        val file = File(context.cacheDir, "Berita_Acara_${berita.nomorSurat.replace("/", "_")}.pdf")
        FileOutputStream(file).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return file
    }

    fun shareBeritaAcaraPdf(context: Context, berita: BeritaAcara) {
        try {
            val pdfFile = generateBeritaAcaraPdf(context, berita)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_SUBJECT, "Dokumen Berita Acara ${berita.nomorSurat}")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Bagikan PDF Berita Acara via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal membagikan PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun downloadBeritaAcaraPdf(context: Context, berita: BeritaAcara) {
        try {
            val pdfFile = generateBeritaAcaraPdf(context, berita)
            val docsFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                ?: context.filesDir
            if (!docsFolder.exists()) docsFolder.mkdirs()

            val destFile = File(docsFolder, "Berita_Acara_${berita.nomorSurat.replace("/", "_")}.pdf")
            pdfFile.copyTo(destFile, overwrite = true)

            Toast.makeText(context, "PDF tersimpan: ${destFile.name}", Toast.LENGTH_LONG).show()

            // Open/View PDF
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", destFile)
            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(viewIntent, "Buka PDF"))
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal mengunduh PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawWrappedText(
        canvas: android.graphics.Canvas,
        text: String,
        x: Float,
        y: Float,
        maxWidth: Float,
        paint: Paint,
        lineHeight: Float
    ): Float {
        var currentY = y
        val words = text.split(" ")
        var line = ""

        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            val measure = paint.measureText(testLine)
            if (measure > maxWidth) {
                canvas.drawText(line, x, currentY, paint)
                line = word
                currentY += lineHeight
            } else {
                line = testLine
            }
        }
        if (line.isNotEmpty()) {
            canvas.drawText(line, x, currentY, paint)
            currentY += lineHeight
        }
        return currentY
    }

    private fun drawSaksiPdfBlock(
        canvas: android.graphics.Canvas,
        jabatan: String,
        nama: String,
        ttdData: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        borderPaint: Paint,
        strokePaint: Paint
    ) {
        val bgPaint = Paint().apply {
            color = Color.parseColor("#F9F9F9")
            style = Paint.Style.FILL
        }
        canvas.drawRect(x, y, x + w, y + h, bgPaint)
        canvas.drawRect(x, y, x + w, y + h, borderPaint)

        val paintTitle = Paint().apply {
            color = Color.DKGRAY
            textSize = 9f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val paintName = Paint().apply {
            color = Color.BLACK
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val centerX = x + w / 2f
        canvas.drawText(jabatan, centerX, y + 16f, paintTitle)

        // Draw Signature Lines if present
        val lines = SignatureUtils.deserializeStrokes(ttdData)
        if (lines.isNotEmpty()) {
            var minX = Float.MAX_VALUE
            var maxX = Float.MIN_VALUE
            var minY = Float.MAX_VALUE
            var maxY = Float.MIN_VALUE

            lines.forEach { l ->
                l.points.forEach { pt ->
                    if (pt.x < minX) minX = pt.x
                    if (pt.x > maxX) maxX = pt.x
                    if (pt.y < minY) minY = pt.y
                    if (pt.y > maxY) maxY = pt.y
                }
            }

            val strokeW = (maxX - minX).coerceAtLeast(1f)
            val strokeH = (maxY - minY).coerceAtLeast(1f)
            val boxW = w * 0.7f
            val boxH = 32f

            val scale = minOf(boxW / strokeW, boxH / strokeH)
            val offsetX = x + (w - strokeW * scale) / 2f - minX * scale
            val offsetY = y + 24f + (boxH - strokeH * scale) / 2f - minY * scale

            lines.forEach { l ->
                if (l.points.size > 1) {
                    val path = android.graphics.Path()
                    path.moveTo(l.points[0].x * scale + offsetX, l.points[0].y * scale + offsetY)
                    for (i in 1 until l.points.size) {
                        path.lineTo(l.points[i].x * scale + offsetX, l.points[i].y * scale + offsetY)
                    }
                    canvas.drawPath(path, strokePaint)
                }
            }
        } else {
            val dashPaint = Paint().apply {
                color = Color.GRAY
                textSize = 9f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("( Belum TTD )", centerX, y + 42f, dashPaint)
        }

        canvas.drawText("( $nama )", centerX, y + h - 10f, paintName)
    }
}
