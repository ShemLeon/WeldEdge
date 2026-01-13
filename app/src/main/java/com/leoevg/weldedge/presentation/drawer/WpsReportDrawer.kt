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
        
        currentY += 20f

        // 4. Cons / Insert, Flux, Sup Filler Table
        val fillerWeights = listOf(1.2f, 1.5f, 2.0f, 0.7f, 0.7f, 2.0f, 1.2f, 1.2f)
        val consTable = Table(
            columns = 8,
            rows = 3,
            data = listOf(
                listOf("Cons / Insert", "", "", "", "", "", "", ""),
                listOf("Flux", "", "", "", "", "", "", ""),
                listOf("Sup Filler", "", "", "", "", "", "", "")
            ),
            columnWeights = fillerWeights,
            columnAligns = List(8) { if (it == 0) Paint.Align.LEFT else Paint.Align.CENTER },
            config = tableConfig
        )
        currentY = drawer.drawTable(canvas, consTable, margin, currentY, tableWidth)

        // 5. Joint Details Table
        val startYForDetails = currentY + 30f
        val jointDetailsTable = Table(
            columns = 2,
            rows = 6,
            headerTitle = "Joint Details",
            data = listOf(
                listOf("Joint Type", "BW or FW"),
                listOf("Groove Angle (Deg)", "60"),
                listOf("Root Opening (mm)", "1.6"),
                listOf("Root Face (mm)", "0.5"),
                listOf("Back gouging", "NO"),
                listOf("Method", "")
            ),
            columnWeights = listOf(1.5f, 1f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER),
            config = tableConfig
        )
        val yAfterJointDetails = drawer.drawTable(canvas, jointDetailsTable, margin, startYForDetails, tableWidth * 0.4f)

        // 6. Postweld Heat Treatment Table
        val startYForPwht = yAfterJointDetails + 30f
        val pwhtTable = Table(
            columns = 2,
            rows = 3,
            headerTitle = "Postweld Heat Treatment",
            data = listOf(
                listOf("Temperature (C°)", ""),
                listOf("Time at Temperature (Min)", ""),
                listOf("Other", "")
            ),
            columnWeights = listOf(1.5f, 1f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER),
            config = tableConfig
        )
        val yAfterPwht = drawer.drawTable(canvas, pwhtTable, margin, startYForPwht, tableWidth * 0.4f)
        
        // 7. Joint Details Sketch (Right Side)
        drawJointDetailsSketch(canvas, margin + tableWidth * 0.45f, startYForDetails, tableWidth * 0.55f)

        val yAfterSketch = startYForDetails + 200f
        currentY = maxOf(yAfterPwht, yAfterSketch) + 30f

        // 8. Large Parameters Table (Weld Layers, Electrical, Technique)
        drawLargeParametersTable(canvas, drawer, tableConfig, margin, currentY, tableWidth)
    }

    private fun drawLargeParametersTable(canvas: Canvas, drawer: TableDrawer, config: TableConfig, x: Float, y: Float, width: Float) {
        var currentY = y
        val colWeights3 = listOf(5f, 1f, 4f)
        val colAligns3 = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.LEFT)

        // Part A: Weld Layers / Passes
        val weldLayersTable = Table(
            columns = 3,
            rows = 17,
            data = listOf(
                listOf("Weld Layers / Passes", "", "1-5"),
                listOf("Process", "", "GTAW"),
                listOf("Type (Manual / Semiautomatic / Automatic)", "", "Manual"),
                listOf("Preheat Temperature (C°), Range 56", "", "20"),
                listOf("Interpass Temperature (C°), Range 56", "", "150"),
                listOf("Filler Metal (AWS Spe)", "", "A5.18"),
                listOf("AWS Classification", "", "ER316L"),
                listOf("F No / A No", "", "6"),
                listOf("Nominal composition", "", ""),
                listOf("Manufacturer / Trade name", "", "ZIKA"),
                listOf("Filler Metal Diameter (mm)", "", "1.6"),
                listOf("Deposited Thickness (mm)", "", ""),
                listOf("Mas Pass Thickness (mm)", "", ""),
                listOf("Position", "", "F for BW / F, H for FW"),
                listOf("Vertical Progression (Up / Dune)", "", "-"),
                listOf("Shielding Gas compos", "", "Ar 99.999%"),
                listOf("Flow Rate (L / Min), Range: 80 - 150%", "", "14-15")
            ),
            columnWeights = colWeights3,
            columnAligns = colAligns3,
            config = config
        )
        currentY = drawer.drawTable(canvas, weldLayersTable, x, currentY, width)

        // Part B: Electrical Characteristics
        val electricalTable = Table(
            columns = 3,
            rows = 9,
            headerTitle = "Electrical Characteristics",
            data = listOf(
                listOf("Electrode Diameter (GTAW)", "", "Red 3.2"),
                listOf("Electrode Specification (GTAW)", "", "WT20"),
                listOf("Multiple or Single Electrode", "", "Single"),
                listOf("Current Type & Polarity", "", "DC-"),
                listOf("Amps (A): GTAW ±5%, GMAW / FCAW ±10%", "", "120"),
                listOf("Volts (V): GTAW ±5%", "", "12-14"),
                listOf("Cold or hote wire feed (GTAW)", "Cold", ""),
                listOf("Travel Speed (mm/Min): GTAW ±5%, GMAW / FCAW ±10%", "", ""),
                listOf("Maximum Heat Input (KJ/mm)", "", "1")
            ),
            columnWeights = colWeights3,
            columnAligns = colAligns3,
            config = config
        )
        currentY = drawer.drawTable(canvas, electricalTable, x, currentY, width)

        // Part C: Technique
        val techniqueTable = Table(
            columns = 5,
            rows = 10,
            headerTitle = "Technique",
            data = listOf(
                listOf("Cap or Nozzle Size (mm)", "", "16", "", ""),
                listOf("Wire Feed Speed", "", "-", "", ""),
                listOf("Stringer or Weave", "", "Stringer", "", ""),
                listOf("Multi or Single Pass (per side)", "", "Multi or Single", "", ""),
                listOf("Oscillation (Mech/Auto)", "", "Auto", "", ""),
                listOf("Transfer Length / Speed", "", "-", "", ""),
                listOf("Dwell Time", "", "-", "", ""),
                listOf("Peening", "", "No", "", ""),
                listOf("Interpass Cleaning", "", "Yes", "", ""),
                listOf("Other", "Cleaning oil and rusty", "", "", "")
            ),
            columnWeights = listOf(5f, 2.5f, 1.5f, 1f, 1f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER),
            config = config
        )
        drawer.drawTable(canvas, techniqueTable, x, currentY, width)
    }

    private fun drawJointDetailsSketch(canvas: Canvas, x: Float, y: Float, width: Float) {
        val rowHeight = 22f
        val totalHeight = 200f
        
        val paint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = 1f }
        val textPaint = Paint().apply { color = Color.BLACK; textSize = 10f; isFakeBoldText = true; isAntiAlias = true; textAlign = Paint.Align.CENTER }

        canvas.drawRect(x, y, x + width, y + totalHeight, paint)
        canvas.drawLine(x, y + rowHeight, x + width, y + rowHeight, paint)
        canvas.drawText("Joint Details (Sketch)", x + width / 2, y + rowHeight / 2 + 3f, textPaint)
        
        try {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.razdelka_single_v_butt_weld_with_root_face_and_root_gap)
            bitmap?.let {
                val padding = 10f
                val dest = RectF(x + padding, y + rowHeight + padding, x + width - padding, y + totalHeight - padding)
                val bitmapWidth = it.width.toFloat()
                val bitmapHeight = it.height.toFloat()
                val ratio = minOf(dest.width() / bitmapWidth, dest.height() / bitmapHeight)
                val finalWidth = bitmapWidth * ratio
                val finalHeight = bitmapHeight * ratio
                val offsetLeft = (dest.width() - finalWidth) / 2
                val offsetTop = (dest.height() - finalHeight) / 2
                val renderRect = RectF(dest.left + offsetLeft, dest.top + offsetTop, dest.left + offsetLeft + finalWidth, dest.top + offsetTop + finalHeight)
                canvas.drawBitmap(it, null, renderRect, null)
            }
        } catch (e: Exception) {}
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

        val paint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = config.borderWidth }
        val textPaint = Paint().apply { color = Color.BLACK; textSize = config.headerTextSize; isFakeBoldText = true; isAntiAlias = true; textAlign = Paint.Align.CENTER }

        var currentX = x
        canvas.drawRect(currentX, y, currentX + colWidths[0], y + rowHeight, paint)
        canvas.drawText("Filler Metal", currentX + colWidths[0] / 2, y + rowHeight / 2 + config.headerTextSize / 3, textPaint)
        currentX += colWidths[0]

        val middlePartWidth = colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4] + colWidths[5]
        canvas.drawRect(currentX, y, currentX + middlePartWidth, y + rowHeight, paint)
        currentX += middlePartWidth

        val lastTwoWidth = colWidths[6] + colWidths[7]
        canvas.drawRect(currentX, y, currentX + lastTwoWidth, y + rowHeight, paint)
        canvas.drawText("Thickness Range", currentX + lastTwoWidth / 2, y + rowHeight / 2 + config.headerTextSize / 3, textPaint)

        val fillerMetalTable = Table(
            columns = 8,
            rows = 1,
            headerRow = listOf("Process", "AWS Spec", "AWS Classification", "F-No", "A-No", "Trade Name", "As Weld", "With PWHT"),
            data = listOf(listOf("GTAW", "ER316L", "A5.9", "6", "", "", "2-6", "")),
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
