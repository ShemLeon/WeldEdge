package com.leoevg.weldedge.presentation.drawer

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.Table
import com.leoevg.weldedge.domain.TableConfig

class WpsReportDrawer(private val context: Context) {

    fun drawReport(canvas: Canvas, pageWidth: Int, pageHeight: Int) {
        val margin = 40f
        val tableWidth = pageWidth - 2 * margin
        var currentY = margin

        // 1. Draw Custom Header
        currentY = drawReportHeader(canvas, margin, currentY, tableWidth)
        currentY += 20f

        val tableConfig = TableConfig(
            headerBackgroundColor = Color.WHITE,
            textSize = 9f,
            headerTextSize = 9f,
            cellPaddingVertical = 10f,
            headerBold = false
        )
        val drawer = TableDrawer(tableConfig)

        // 2. BASE METAL & THICKNESS (Side by Side)
        val spacing = 15f
        val leftWidth = tableWidth * 0.55f
        val rightWidth = tableWidth - leftWidth - spacing

        val baseMetalTable = Table(
            columns = 5,
            rows = 5,
            headerRow = listOf("Base Metal", "Specification", "Type or Grade", "M", "AWS Group No."),
            data = listOf(
                listOf("Base Material", "SS316L", "", "", ""),
                listOf("Welded To", "SS316L", "", "", ""),
                listOf("Backing Material", "", "", "", ""),
                listOf("Other", "", "", "", ""),
                listOf("", "", "", "", "")
            ),
            columnWeights = listOf(2.2f, 1.8f, 1.2f, 0.4f, 1.2f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER),
            config = tableConfig
        )
        val yAfterBaseMetal = drawer.drawTable(canvas, baseMetalTable, margin, currentY, leftWidth)

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
        val yAfterThickness = drawer.drawTable(canvas, thickTable, margin + leftWidth + spacing, currentY, rightWidth)

        currentY = maxOf(yAfterBaseMetal, yAfterThickness) + 20f

        // 3. FILLER METAL TABLE with complex header
        currentY = drawFillerMetalWithComplexHeader(canvas, drawer, tableConfig, margin, currentY, tableWidth)
    }

    private fun drawFillerMetalWithComplexHeader(
        canvas: Canvas,
        drawer: TableDrawer,
        config: TableConfig,
        x: Float,
        y: Float,
        width: Float
    ): Float {
        val rowHeight = config.textSize + (2 * config.cellPaddingVertical)
        val weights = listOf(1.2f, 1.5f, 2.0f, 0.7f, 0.7f, 2.0f, 1.2f, 1.2f)
        val totalWeight = weights.sum()
        val colWidths = weights.map { (it / totalWeight) * width }

        val paint = Paint().apply { 
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = config.borderWidth 
        }
        val textPaint = Paint().apply { 
            color = Color.BLACK
            textSize = config.headerTextSize
            isFakeBoldText = true 
            isAntiAlias = true
            textAlign = Paint.Align.CENTER 
        }

        var currentX = x
        
        // 1. Первая ячейка: Filler Metal
        canvas.drawRect(currentX, y, currentX + colWidths[0], y + rowHeight, paint)
        canvas.drawText("Filler Metal", currentX + colWidths[0] / 2, y + rowHeight / 2 + config.headerTextSize / 3, textPaint)
        currentX += colWidths[0]

        // 2. Ячейки со 2-й по 6-ю: Пустые
        for (i in 1..5) {
            canvas.drawRect(currentX, y, currentX + colWidths[i], y + rowHeight, paint)
            currentX += colWidths[i]
        }

        // 3. Последние две ячейки: Thickness Range (объединенные)
        val lastTwoWidth = colWidths[6] + colWidths[7]
        canvas.drawRect(currentX, y, currentX + lastTwoWidth, y + rowHeight, paint)
        canvas.drawText("Thickness Range", currentX + lastTwoWidth / 2, y + rowHeight / 2 + config.headerTextSize / 3, textPaint)

        // Отрисовка остальной части таблицы через TableDrawer
        val fillerMetalTable = Table(
            columns = 8,
            rows = 1, // БЫЛО 2, СТАЛО 1
            headerRow = listOf("Process", "AWS Spec", "AWS Classification", "F-No", "A-No", "Trade Name", "As Weld", "With PWHT"),
            data = listOf(
                listOf("GTAW", "ER316L", "A5.9", "6", "", "", "2-6", "")
                // Удалили пустой listOf
            ),
            columnWeights = weights,
            columnAligns = List(8) { if (it == 0) Paint.Align.LEFT else Paint.Align.CENTER },
            config = config
        )

        return drawer.drawTable(canvas, fillerMetalTable, x, y + rowHeight, width)
    }

    private fun drawReportHeader(canvas: Canvas, x: Float, y: Float, width: Float): Float {
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
}
