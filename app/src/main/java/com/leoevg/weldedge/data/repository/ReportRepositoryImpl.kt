package com.leoevg.weldedge.data.repository

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.repository.ReportRepository
import com.leoevg.weldedge.presentation.drawer.WpsReportDrawer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ReportRepository {

    override suspend fun generateAndOpenReport(params: WeldingParams): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val pageWidth = 792
            val pageHeight = 1120
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            WpsReportDrawer(context).drawReport(page.canvas, pageWidth, pageHeight, params)

            pdfDocument.finishPage(page)

            val date = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())
            val fileName = "WPS_${params.metalType}_${params.jointType}_${date}.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            withContext(Dispatchers.Main) {
                context.startActivity(intent)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
