package com.leoevg.weldedge

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.leoevg.weldedge.domain.Table
import com.leoevg.weldedge.domain.TableConfig
import com.leoevg.weldedge.presentation.drawer.TableDrawer
import com.leoevg.weldedge.ui.theme.WeldEdgeTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { WeldEdgeTheme { Surface { MainScreen() } } }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { generateProfessionalWpsReport(context) }) {
            Text("Generate Professional WPS Report")
        }
    }
}

fun generateProfessionalWpsReport(context: Context) {
    val pageWidth = 792
    val pageHeight = 1120
    val pdfDocument = PdfDocument()
    val page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create())
    val canvas = page.canvas

    val margin = 40f
    val tableWidth = pageWidth - 2 * margin
    var currentY = margin

    currentY = drawReportHeader(canvas, context, margin, currentY, tableWidth)
    currentY += 20f

    val spacing = 15f
    val leftWidth = tableWidth * 0.55f
    val rightWidth = tableWidth - leftWidth - spacing

    val tableConfig = TableConfig(
        headerBackgroundColor = Color.WHITE,
        textSize = 9f,
        headerTextSize = 9f,
        cellPaddingVertical = 10f,
        headerBold = false
    )

    // --- LEFT TABLE: BASE METAL ---
    val baseMetalTable = Table(
        columns = 5,
        rows = 5, // Increased to 5 to accommodate the empty row
        headerRow = listOf("Base Metal", "Specification", "Type or Grade", "M", "AWS Group No."),
        data = listOf(
            listOf("Base Material", "SS316L", "", "", ""),
            listOf("Welded To", "SS316L", "", "", ""),
            listOf("Backing Material", "", "", "", ""),
            listOf("Other", "", "", "", ""),
            listOf("", "", "", "", "") // Empty row for future notes
        ),
        columnWeights = listOf(2.2f, 1.8f, 1.2f, 0.4f, 1.2f),
        columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER),
        config = tableConfig
    )
    TableDrawer(tableConfig).drawTable(canvas, baseMetalTable, margin, currentY, leftWidth)

    // --- RIGHT TABLE: THICKNESS ---
    val thickTable = Table(
        columns = 3,
        rows = 5,
        headerRow = listOf("BASE METAL THICKNESS (mm)", "As Weld Thickness Range", "With PWHT Thickness Range"),
        data = listOf(
            listOf("CJP Groove Weld", "2-6", ""),
            listOf("CJP Groove Weld w/CVN", "", ""),
            listOf("PJP Groove Weld", "2-6", ""),
            listOf("Fillet Weld", "2-6", ""),
            listOf("Pipe / Tube Diameter Range", "", "")
        ),
        columnWeights = listOf(2.2f, 1f, 1f),
        columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.CENTER),
        config = tableConfig
    )
    TableDrawer(tableConfig).drawTable(canvas, thickTable, margin + leftWidth + spacing, currentY, rightWidth)

    pdfDocument.finishPage(page)
    saveAndOpenPdf(context, pdfDocument)
}

private fun drawReportHeader(canvas: Canvas, context: Context, x: Float, y: Float, width: Float): Float {
    val rowHeight = 22f
    val logoWidth = 110f
    val dataWidth = width - logoWidth
    val colWidth = dataWidth / 4
    val paint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = 1f }
    val textPaint = Paint().apply { color = Color.BLACK; textSize = 10f; isAntiAlias = true; textAlign = Paint.Align.CENTER }
    val boldPaint = Paint(textPaint).apply { isFakeBoldText = true; textSize = 11f }
    val italicPaint = Paint(textPaint).apply { textSkewX = -0.2f }

    canvas.drawRect(x, y, x + width, y + rowHeight * 3, paint)
    canvas.drawLine(x + logoWidth, y, x + logoWidth, y + rowHeight * 3, paint)

    try {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_san)
        bitmap?.let {
            val dest = RectF(x + 10f, y + 5f, x + logoWidth - 10f, y + rowHeight * 3 - 5f)
            canvas.drawBitmap(it, null, dest, null)
        }
    } catch (e: Exception) {}

    canvas.drawLine(x + logoWidth, y + rowHeight, x + width, y + rowHeight, paint)
    canvas.drawText("Welding Procedure Specification WPS 3092 (013)", x + logoWidth + dataWidth / 2, y + rowHeight - 7f, boldPaint)

    canvas.drawLine(x + logoWidth, y + rowHeight * 2, x + width, y + rowHeight * 2, paint)
    val values = listOf("SAN", "3092", "04", "26/6/22")
    for (i in 0..3) {
        val cellX = x + logoWidth + i * colWidth
        if (i > 0) canvas.drawLine(cellX, y + rowHeight, cellX, y + rowHeight * 3, paint)
        canvas.drawText(values[i], cellX + colWidth / 2, y + rowHeight * 2 - 7f, italicPaint)
    }

    val labels = listOf("Company Name", "PQR No", "Rev. No", "Date")
    for (i in 0..3) {
        val cellX = x + logoWidth + i * colWidth
        canvas.drawText(labels[i], cellX + colWidth / 2, y + rowHeight * 3 - 7f, textPaint)
    }
    return y + rowHeight * 3
}

private fun saveAndOpenPdf(context: Context, doc: PdfDocument) {
    val file = File(context.getExternalFilesDir(null), "WeldEdge_Pro_Report.pdf")
    try {
        doc.writeTo(FileOutputStream(file))
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    } finally {
        doc.close()
    }
}
