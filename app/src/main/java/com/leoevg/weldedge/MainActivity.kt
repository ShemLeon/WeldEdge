package com.leoevg.weldedge

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import com.leoevg.weldedge.domain.WeldingParams
import com.leoevg.weldedge.presentation.drawer.WpsReportDrawer
import com.leoevg.weldedge.presentation.screen.main.MainScreen
import com.leoevg.weldedge.ui.theme.WeldEdgeTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeldEdgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

fun generateProfessionalWpsReport(context: Context, params: WeldingParams) {
    val pageWidth = 792
    val pageHeight = 1120
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    
    WpsReportDrawer(context).drawReport(page.canvas, pageWidth, pageHeight, params)

    pdfDocument.finishPage(page)
    saveAndOpenPdf(context, pdfDocument, params)
}

private fun saveAndOpenPdf(context: Context, doc: PdfDocument, params: WeldingParams) {
    val date = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())
    val fileName = "WPS_${params.metalType}_${params.jointType}_${date}.pdf"
    val file = File(context.getExternalFilesDir(null), fileName)
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
