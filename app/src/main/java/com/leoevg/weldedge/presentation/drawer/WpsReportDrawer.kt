package com.leoevg.weldedge.presentation.drawer

import android.content.Context
import android.graphics.*
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.Table
import com.leoevg.weldedge.domain.model.TableConfig
import com.leoevg.weldedge.domain.model.WeldingParams
import com.caverock.androidsvg.SVG
import java.io.InputStream

class WpsReportDrawer(private val context: Context) {

    fun drawReport(canvas: Canvas, pageWidth: Int, pageHeight: Int, params: WeldingParams) {
        val margin = 40f
        val tableWidth = pageWidth - 2 * margin
        var currentY = margin

        // 1. Draw Custom Header (Super Compact)
        currentY = drawReportHeader(canvas, margin, currentY, tableWidth, params)
        currentY += 8f 

        val tableConfig = TableConfig(
            headerBackgroundColor = Color.WHITE,
            textSize = 8.5f, 
            headerTextSize = 8.5f,
            cellPaddingVertical = 3.5f, 
            headerBold = true
        )
        val drawer = TableDrawer(tableConfig)

        // 2. BASE METAL & THICKNESS
        val spacing = 10f
        val leftWidth = tableWidth * 0.55f
        val rightWidth = tableWidth - leftWidth - spacing

        val baseMetalTable = Table(
            columns = 5,
            rows = 5,
            headerRow = listOf("Base Metal", "Specification", "Type or Grade", "M", "AWS Group No."),
            data = listOf(
                listOf("Base Material", params.metalType, "", "", ""),
                listOf("Welded To", params.metalType, "", "", ""),
                listOf("Backing Material", "", "", "", ""),
                listOf("Other", "Standard: ${params.standard}", "", "", ""),
                listOf("", "", "", "", "")
            ),
            columnWeights = listOf(2.2f, 1.8f, 1.2f, 0.4f, 1.2f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER, Paint.Align.CENTER),
            config = tableConfig.copy(headerBold = false)
        )
        val yAfterBaseMetal = drawer.drawTable(canvas, baseMetalTable, margin, currentY, leftWidth)

        val thickTable = Table(
            columns = 3,
            rows = 5,
            headerRow = listOf("BASE METAL THICKNESS (mm)", "As Weld Range", "With PWHT Range"),
            data = listOf(
                listOf("CJP Groove Weld", params.thickness, ""),
                listOf("CJP Groove Weld w/CVN", "", ""),
                listOf("PJP Groove Weld", params.thickness, ""),
                listOf("Fillet Weld", params.thickness, ""),
                listOf("Pipe / Tube Diameter Range", "", "")
            ),
            columnWeights = listOf(2.2f, 1f, 1f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER, Paint.Align.CENTER),
            config = tableConfig.copy(headerBold = false)
        )
        val yAfterThickness = drawer.drawTable(canvas, thickTable, margin + leftWidth + spacing, currentY, rightWidth)

        currentY = maxOf(yAfterBaseMetal, yAfterThickness) + 8f

        // 3. FILLER METAL TABLE
        currentY = drawFillerMetalWithComplexHeader(canvas, drawer, tableConfig, margin, currentY, tableWidth)
        
        currentY += 8f

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
        val startYForDetails = currentY + 8f
        val jointDetailsTable = Table(
            columns = 2,
            rows = 6,
            headerTitle = "Joint Details",
            data = listOf(
                listOf("Joint Type", params.jointType.replaceFirstChar { it.uppercase() }),
                listOf("Groove Angle (Deg)", "60"),
                listOf("Root Opening (mm)", "1.6"),
                listOf("Root Face (mm)", "0.5"),
                listOf("Back gouging", "NO"),
                listOf("Responsibility", params.responsibility)
            ),
            columnWeights = listOf(1.5f, 1f),
            columnAligns = listOf(Paint.Align.LEFT, Paint.Align.CENTER),
            config = tableConfig
        )
        val yAfterJointDetails = drawer.drawTable(canvas, jointDetailsTable, margin, startYForDetails, tableWidth * 0.35f)

        // 6. Postweld Heat Treatment Table
        val startYForPwht = yAfterJointDetails + 8f
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
        val yAfterPwht = drawer.drawTable(canvas, pwhtTable, margin, startYForPwht, tableWidth * 0.35f)
        
        // 7. Joint Details Sketch
        drawJointDetailsSketch(canvas, margin + tableWidth * 0.4f, startYForDetails, tableWidth * 0.6f, params)

        val yAfterSketch = startYForDetails + 150f 
        currentY = maxOf(yAfterPwht, yAfterSketch) + 8f

        // 8. Large Parameters Table
        val finalY = drawLargeParametersTable(canvas, drawer, tableConfig, margin, currentY, tableWidth)

        // 9. Footer Text
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 9f
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        val engineerText = if (params.engineerName.isNotEmpty()) "Engineer: ${params.engineerName} | " else ""
        canvas.drawText("${engineerText}1 list from 1", margin, finalY + 20f, footerPaint)
    }

    private fun drawLargeParametersTable(canvas: Canvas, drawer: TableDrawer, config: TableConfig, x: Float, y: Float, width: Float): Float {
        var curY = y
        val colWeights2 = listOf(1.5f, 1f)
        val colAligns2 = listOf(Paint.Align.LEFT, Paint.Align.CENTER)

        val weldLayersTable = Table(
            columns = 2,
            rows = 17,
            data = listOf(
                listOf("Weld Layers / Passes", "1-5"),
                listOf("Process", "GTAW"),
                listOf("Type (Manual / Semiautomatic / Automatic)", "Manual"),
                listOf("Preheat Temperature (C°)", "20"),
                listOf("Interpass Temperature (C°)", "150"),
                listOf("Filler Metal (AWS Spe)", "A5.18"),
                listOf("AWS Classification", "ER316L"),
                listOf("F No / A No", "6"),
                listOf("Nominal composition", ""),
                listOf("Manufacturer / Trade name", "ZIKA"),
                listOf("Filler Metal Diameter (mm)", "1.6"),
                listOf("Deposited Thickness (mm)", ""),
                listOf("Mas Pass Thickness (mm)", ""),
                listOf("Position", "F for BW / F, H for FW"),
                listOf("Vertical Progression (Up / Dune)", "-"),
                listOf("Shielding Gas compos", "Ar 99.999%"),
                listOf("Flow Rate (L / Min)", "14-15")
            ),
            columnWeights = colWeights2,
            columnAligns = colAligns2,
            config = config
        )
        curY = drawer.drawTable(canvas, weldLayersTable, x, curY, width)

        val electricalTable = Table(
            columns = 2,
            rows = 9,
            headerTitle = "Electrical Characteristics",
            data = listOf(
                listOf("Electrode Diameter (GTAW)", "Red 3.2"),
                listOf("Electrode Specification (GTAW)", "WT20"),
                listOf("Multiple or Single Electrode", "Single"),
                listOf("Current Type & Polarity", "DC-"),
                listOf("Amps (A)", "120"),
                listOf("Volts (V)", "12-14"),
                listOf("Cold or hote wire feed (GTAW)", "Cold"),
                listOf("Travel Speed (mm/Min)", ""),
                listOf("Maximum Heat Input (KJ/mm)", "1")
            ),
            columnWeights = colWeights2,
            columnAligns = colAligns2,
            config = config.copy(headerBold = true)
        )
        curY = drawer.drawTable(canvas, electricalTable, x, curY, width)

        val techniqueTable = Table(
            columns = 2,
            rows = 10,
            headerTitle = "Technique",
            data = listOf(
                listOf("Cap or Nozzle Size (mm)", "16"),
                listOf("Wire Feed Speed", "-"),
                listOf("Stringer or Weave", "Stringer"),
                listOf("Multi or Single Pass (per side)", "Multi or Single"),
                listOf("Oscillation (Mech/Auto)", "Auto"),
                listOf("Transfer Length / Speed", "-"),
                listOf("Dwell Time", "-"),
                listOf("Peening", "No"),
                listOf("Interpass Cleaning", "Yes"),
                listOf("Other", "")
            ),
            columnWeights = colWeights2,
            columnAligns = colAligns2,
            config = config.copy(headerBold = true)
        )
        return drawer.drawTable(canvas, techniqueTable, x, curY, width)
    }

    private fun drawJointDetailsSketch(canvas: Canvas, x: Float, y: Float, width: Float, params: WeldingParams) {
        val rowHeight = 16f
        val totalHeight = 150f

        val paint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = 0.8f }
        val textPaint = Paint().apply { color = Color.BLACK; textSize = 8.5f; isFakeBoldText = true; isAntiAlias = true; textAlign = Paint.Align.CENTER }

        canvas.drawRect(x, y, x + width, y + totalHeight, paint)
        canvas.drawLine(x, y + rowHeight, x + width, y + rowHeight, paint)
        canvas.drawText("Joint Details (Sketch)", x + width / 2, y + rowHeight / 2 + 3f, textPaint)

        try {
            if (params.edgePreparation.isNotEmpty()) {
                val folder = when (params.jointType) {
                    "стык" -> "groove"
                    "тавр" -> "t"
                    "нахлест" -> "lap"
                    "угловое" -> "corner"
                    else -> "groove"
                }
                val subFolder = if (params.responsibility == "нагруженный") "stress" else "simple"
                val fullPath = "edge_preparation/$folder/$subFolder/${params.edgePreparation}"

                val inputStream = context.assets.open(fullPath)
                val svg = SVG.getFromInputStream(inputStream)
                inputStream.close()

                svg?.let {
                    val padding = 15f
                    val destWidth = width - 2 * padding
                    val destHeight = totalHeight - rowHeight - 2 * padding

                    // Устанавливаем размеры SVG
                    it.setDocumentWidth("100%")
                    it.setDocumentHeight("100%")

                    // Создаем Bitmap для рендеринга
                    val bitmap = Bitmap.createBitmap(destWidth.toInt(), destHeight.toInt(), Bitmap.Config.ARGB_8888)
                    val svgCanvas = Canvas(bitmap)
                    svgCanvas.drawColor(Color.WHITE)

                    // Рендерим SVG на Canvas
                    it.renderToCanvas(svgCanvas)

                    // Рисуем Bitmap на основном Canvas
                    val dest = RectF(x + padding, y + rowHeight + padding, x + width - padding, y + totalHeight - padding)
                    canvas.drawBitmap(bitmap, null, dest, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

        val paint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = 0.8f }
        val textPaint = Paint().apply { color = Color.BLACK; textSize = 8.5f; isFakeBoldText = true; isAntiAlias = true; textAlign = Paint.Align.CENTER }

        var currentX = x
        canvas.drawRect(currentX, y, currentX + colWidths[0], y + rowHeight, paint)
        canvas.drawText("Filler Metal", currentX + colWidths[0] / 2, y + rowHeight / 2 + 3f, textPaint)
        currentX += colWidths[0]

        val middlePartWidth = colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4] + colWidths[5]
        canvas.drawRect(currentX, y, currentX + middlePartWidth, y + rowHeight, paint)
        currentX += middlePartWidth

        val lastTwoWidth = colWidths[6] + colWidths[7]
        canvas.drawRect(currentX, y, currentX + lastTwoWidth, y + rowHeight, paint)
        canvas.drawText("Thickness Range", currentX + lastTwoWidth / 2, y + rowHeight / 2 + 3f, textPaint)

        val fillerMetalTable = Table(
            columns = 8,
            rows = 1,
            headerRow = listOf("Process", "AWS Spec", "AWS Classification", "F-No", "A-No", "Trade Name", "As Weld", "With PWHT"),
            data = listOf(listOf("GTAW", "ER316L", "A5.9", "6", "", "", "2-6", "")),
            columnWeights = weights,
            columnAligns = List(8) { if (it == 0) Paint.Align.LEFT else Paint.Align.CENTER },
            config = config.copy(headerBold = false)
        )
        return drawer.drawTable(canvas, fillerMetalTable, x, y + rowHeight, width)
    }

    private fun drawReportHeader(canvas: Canvas, x: Float, y: Float, width: Float, params: WeldingParams): Float {
        val rowHeight = 16f 
        val logoWidth = 110f
        val dataWidth = width - logoWidth
        val colWidth = dataWidth / 4
        val paint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = 0.8f }
        val textPaint = Paint().apply { color = Color.BLACK; textSize = 8.5f; isAntiAlias = true; textAlign = Paint.Align.CENTER }
        val boldPaint = Paint(textPaint).apply { isFakeBoldText = true; textSize = 9f }
        val italicPaint = Paint(textPaint).apply { textSkewX = -0.2f }

        canvas.drawRect(x, y, x + width, y + rowHeight * 3, paint)
        canvas.drawLine(x + logoWidth, y, x + logoWidth, y + rowHeight * 3, paint)

        try {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_san)
            bitmap?.let {
                val dest = RectF(x + 10f, y + 2f, x + logoWidth - 10f, y + rowHeight * 3 - 2f)
                canvas.drawBitmap(it, null, dest, null)
            }
        } catch (e: Exception) {}

        canvas.drawLine(x + logoWidth, y + rowHeight, x + width, y + rowHeight, paint)
        canvas.drawText("Welding Procedure Specification (WPS)", x + logoWidth + dataWidth / 2, y + rowHeight - 4f, boldPaint)

        canvas.drawLine(x + logoWidth, y + rowHeight * 2, x + width, y + rowHeight * 2, paint)
        val date = java.text.SimpleDateFormat("dd/MM/yy", java.util.Locale.getDefault()).format(java.util.Date())
        val values = listOf("WeldEdge", "PQR-001", "00", date)
        for (i in 0..3) {
            val cellX = x + logoWidth + i * colWidth
            if (i > 0) canvas.drawLine(cellX, y + rowHeight, cellX, y + rowHeight * 3, paint)
            canvas.drawText(values[i], cellX + colWidth / 2, y + rowHeight * 2 - 4f, italicPaint)
        }

        val labels = listOf("Company Name", "PQR No", "Rev. No", "Date")
        for (i in 0..3) {
            val cellX = x + logoWidth + i * colWidth
            canvas.drawText(labels[i], cellX + colWidth / 2, y + rowHeight * 3 - 4f, textPaint)
        }
        return y + rowHeight * 3
    }
}
