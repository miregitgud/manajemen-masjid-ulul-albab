package com.mesjidululalbab.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.ui.theme.Emas
import com.mesjidululalbab.ui.theme.Hijau

data class StrokeLine(
    val points: List<Offset>
)

object SignatureUtils {

    fun serializeStrokes(lines: List<StrokeLine>): String {
        return lines.joinToString(separator = "|") { line ->
            line.points.joinToString(separator = ";") { pt -> "${pt.x},${pt.y}" }
        }
    }

    fun deserializeStrokes(str: String): List<StrokeLine> {
        if (str.isBlank()) return emptyList()
        return str.split("|").mapNotNull { lineStr ->
            val points = lineStr.split(";").mapNotNull { ptStr ->
                val coords = ptStr.split(",")
                if (coords.size == 2) {
                    val x = coords[0].toFloatOrNull()
                    val y = coords[1].toFloatOrNull()
                    if (x != null && y != null) Offset(x, y) else null
                } else null
            }
            if (points.isNotEmpty()) StrokeLine(points) else null
        }
    }
}

@Composable
fun SignatureDialog(
    namaSaksi: String,
    saksiIndex: Int,
    ttdAwal: String,
    onDismiss: () -> Unit,
    onSimpanTtd: (String) -> Unit
) {
    val currentLines = remember {
        mutableStateListOf<StrokeLine>().apply {
            addAll(SignatureUtils.deserializeStrokes(ttdAwal))
        }
    }
    var currentPathPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val serialized = SignatureUtils.serializeStrokes(currentLines)
                    onSimpanTtd(serialized)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Hijau,
                    contentColor = Emas
                )
            ) {
                Text("Simpan Tanda Tangan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { currentLines.clear() }
            ) {
                Text("Hapus/Ulangi", color = MaterialTheme.colorScheme.error)
            }
        },
        title = {
            Text(
                text = "Tanda Tangan Saksi $saksiIndex",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Goreskan tanda tangan $namaSaksi pada kotak di bawah:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        currentPathPoints = listOf(offset)
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        currentPathPoints = currentPathPoints + change.position
                                    },
                                    onDragEnd = {
                                        if (currentPathPoints.isNotEmpty()) {
                                            currentLines.add(StrokeLine(currentPathPoints))
                                            currentPathPoints = emptyList()
                                        }
                                    }
                                )
                            }
                    ) {
                        // Render confirmed lines
                        currentLines.forEach { line ->
                            if (line.points.size > 1) {
                                val path = Path().apply {
                                    moveTo(line.points.first().x, line.points.first().y)
                                    for (i in 1 until line.points.size) {
                                        lineTo(line.points[i].x, line.points[i].y)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    color = Color(0xFF123D2B),
                                    style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )
                            }
                        }

                        // Render active in-flight line
                        if (currentPathPoints.size > 1) {
                            val path = Path().apply {
                                moveTo(currentPathPoints.first().x, currentPathPoints.first().y)
                                for (i in 1 until currentPathPoints.size) {
                                    lineTo(currentPathPoints[i].x, currentPathPoints[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = Color(0xFF123D2B),
                                style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SignatureViewer(
    ttdData: String,
    modifier: Modifier = Modifier
) {
    val lines = remember(ttdData) { SignatureUtils.deserializeStrokes(ttdData) }

    if (lines.isEmpty()) {
        Text("✓ Signed", fontSize = 9.sp, color = Color(0xFF123D2B), fontWeight = FontWeight.Bold)
    } else {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            val canvasW = size.width
            val canvasH = size.height

            // Compute bounding box
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

            val strokeWidth = (maxX - minX).coerceAtLeast(1f)
            val strokeHeight = (maxY - minY).coerceAtLeast(1f)

            val scaleX = (canvasW * 0.8f) / strokeWidth
            val scaleY = (canvasH * 0.8f) / strokeHeight
            val scale = minOf(scaleX, scaleY)

            val offsetX = (canvasW - strokeWidth * scale) / 2f - minX * scale
            val offsetY = (canvasH - strokeHeight * scale) / 2f - minY * scale

            lines.forEach { line ->
                if (line.points.size > 1) {
                    val path = Path().apply {
                        val first = line.points.first()
                        moveTo(first.x * scale + offsetX, first.y * scale + offsetY)
                        for (i in 1 until line.points.size) {
                            val pt = line.points[i]
                            lineTo(pt.x * scale + offsetX, pt.y * scale + offsetY)
                        }
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF123D2B),
                        style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }
    }
}
