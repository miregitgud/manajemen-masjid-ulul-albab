package com.mesjidululalbab.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesjidululalbab.data.model.BeritaAcara
import com.mesjidululalbab.ui.theme.*
import com.mesjidululalbab.util.PdfUtils
import com.mesjidululalbab.util.formatRupiah

@Composable
fun BeritaAcaraDetailDialog(
    berita: BeritaAcara,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
        },
        dismissButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { PdfUtils.downloadBeritaAcaraPdf(context, berita) },
                    border = BorderStroke(1.dp, Emas),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmasTerang)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Unduh PDF",
                        modifier = Modifier.size(16.dp),
                        tint = EmasTerang
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Unduh PDF", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { PdfUtils.shareBeritaAcaraPdf(context, berita) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emas,
                        contentColor = Hijau
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Bagikan PDF",
                        modifier = Modifier.size(16.dp),
                        tint = Hijau
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Bagikan PDF", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        },
        text = {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header / Kop Surat
                    Text(
                        text = "MASJID ULUL ALBAB",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF123D2B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "PT. REKAYASA INDUSTRI • JAKARTA SELATAN",
                        fontSize = 10.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(6.dp))
                    HorizontalDivider(color = Color(0xFF123D2B), thickness = 2.dp)
                    Spacer(Modifier.height(10.dp))

                    // Title
                    Text(
                        text = "BERITA ACARA INFAK JUMAT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Nomor: ${berita.nomorSurat}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))

                    // Statement
                    Text(
                        text = "Pada hari ini ${berita.hari}, tanggal ${berita.tanggal}, telah dilaksanakan perhitungan penerimaan dana Infak Jumat pada Masjid Ulul Albab dengan hasil sebagai berikut:",
                        fontSize = 11.sp,
                        color = Color.Black,
                        lineHeight = 16.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    // Amount Box
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8F5EA)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "JUMLAH TOTAL INFAK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = formatRupiah(berita.jumlahInfak),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF123D2B)
                            )
                            Text(
                                text = "Terbilang: ${berita.terbilang}",
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (berita.keterangan.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Catatan: ${berita.keterangan}",
                            fontSize = 10.sp,
                            color = Color.DarkGray
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Demikian Berita Acara ini dibuat secara sah dan disaksikan oleh empat (4) saksi penandatangan di bawah ini:",
                        fontSize = 10.sp,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(16.dp))

                    // 4 Witness Signature Grid (2 x 2)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SaksiBlock("Saksi I (Ketua DKM)", berita.saksi1, berita.saksi1Ttd, modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(8.dp))
                            SaksiBlock("Saksi II (Bendahara)", berita.saksi2, berita.saksi2Ttd, modifier = Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SaksiBlock("Saksi III (Penghitung 1)", berita.saksi3, berita.saksi3Ttd, modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(8.dp))
                            SaksiBlock("Saksi IV (Penghitung 2)", berita.saksi4, berita.saksi4Ttd, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun SaksiBlock(
    jabatan: String,
    nama: String,
    ttdData: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(0xFFF9F9F9), RoundedCornerShape(6.dp))
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Text(jabatan, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(Modifier.height(6.dp))

        // Render actual handwritten signature or fallback signature line
        if (ttdData.isNotBlank()) {
            SignatureViewer(ttdData = ttdData, modifier = Modifier.height(36.dp))
        } else {
            Spacer(Modifier.height(18.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))
            Text("( Belum TTD )", fontSize = 8.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
        }

        Spacer(Modifier.height(6.dp))
        Text("( $nama )", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}

@Preview
@Composable
fun BeritaAcaraDetailDialogPreview() {
    BeritaAcaraDetailDialog(
        berita = BeritaAcara(
            id = "1",
            nomorSurat = "BA/001/MUA/X/2026",
            hari = "Jumat",
            tanggal = "24/10/2026",
            jumlahInfak = 5500000L,
            terbilang = "Lima Juta Lima Ratus Ribu Rupiah",
            saksi1 = "H. Ahmad",
            saksi2 = "H. Ridwan",
            saksi3 = "Ustadz Faisal",
            saksi4 = "Ir. Bambang",
            keterangan = "Perhitungan dilaksanakan Ba'da Shalat Jumat"
        ),
        onDismiss = {}
    )
}
