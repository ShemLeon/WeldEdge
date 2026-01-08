package com.leoevg.weldedge.presentation.drawer

import android.graphics.*
import com.leoevg.weldedge.domain.Table
import com.leoevg.weldedge.domain.TableConfig

class TableDrawer(private val config: TableConfig = TableConfig()) {

    fun drawTable(
        canvas: Canvas,
        table: Table,
        startX: Float,
        startY: Float,
        maxWidth: Float,
        maxHeight: Float = Float.MAX_VALUE
    ): Float {
        val totalWeight = table.columnWeights?.sum() ?: table.columns.toFloat()
        val columnWidths = table.columnWeights?.map { (it / totalWeight) * maxWidth }
            ?: List(table.columns) { maxWidth / table.columns }

        val rowHeight = calculateRowHeight()
        var currentY = startY

        // Draw header title (spans all columns)
        table.headerTitle?.let { header ->
            currentY = drawHeaderFooterRow(canvas, header, startX, currentY, maxWidth, rowHeight)
        }

        // Draw header row (column headers)
        table.headerRow?.let { headerRow ->
            currentY = drawDataRow(
                canvas,
                headerRow,
                -1, // Special index for header row style
                startX,
                currentY,
                columnWidths,
                rowHeight,
                isHeader = true
            )
        }

        // Draw data rows
        table.data.forEachIndexed { rowIndex, rowData ->
            if (currentY + rowHeight > maxHeight) {
                return currentY
            }

            currentY = drawDataRow(
                canvas,
                rowData,
                rowIndex,
                startX,
                currentY,
                columnWidths,
                rowHeight
            )
        }

        table.footerTitle?.let { footer ->
            currentY = drawHeaderFooterRow(canvas, footer, startX, currentY, maxWidth, rowHeight)
        }

        return currentY
    }

    private fun drawHeaderFooterRow(
        canvas: Canvas,
        title: String,
        startX: Float,
        startY: Float,
        maxWidth: Float,
        rowHeight: Float
    ): Float {
        val textPaint = Paint().apply {
            textSize = config.headerTextSize
            color = config.headerTextColor
            isAntiAlias = true
            isFakeBoldText = config.headerBold
            textAlign = Paint.Align.CENTER
        }

        val backgroundPaint = Paint().apply {
            color = config.headerBackgroundColor
            style = Paint.Style.FILL
        }

        val borderPaint = Paint().apply {
            color = config.borderColor
            strokeWidth = config.borderWidth
            style = Paint.Style.STROKE
        }

        canvas.drawRect(startX, startY, startX + maxWidth, startY + rowHeight, backgroundPaint)
        canvas.drawRect(startX, startY, startX + maxWidth, startY + rowHeight, borderPaint)

        val textX = startX + (maxWidth / 2)
        val textY = startY + (rowHeight / 2) + (config.headerTextSize / 3)

        drawCellText(canvas, title, textX, textY, maxWidth, textPaint)

        return startY + rowHeight
    }

    private fun drawDataRow(
        canvas: Canvas,
        rowData: List<String>,
        rowIndex: Int,
        startX: Float,
        startY: Float,
        columnWidths: List<Float>,
        rowHeight: Float,
        isHeader: Boolean = false
    ): Float {
        val textPaint = Paint().apply {
            textSize = if (isHeader) config.headerTextSize else config.textSize
            color = if (isHeader) config.headerTextColor else config.cellTextColor
            isAntiAlias = true
            isFakeBoldText = isHeader && config.headerBold
            textAlign = Paint.Align.CENTER
        }

        val backgroundPaint = Paint().apply {
            color = when {
                isHeader -> config.headerBackgroundColor
                rowIndex % 2 == 1 && config.alternateRowColor != null -> config.alternateRowColor
                else -> Color.WHITE
            }
            style = Paint.Style.FILL
        }

        val borderPaint = Paint().apply {
            color = config.borderColor
            strokeWidth = config.borderWidth
            style = Paint.Style.STROKE
        }

        var currentX = startX
        rowData.forEachIndexed { colIndex, text ->
            val colWidth = columnWidths[colIndex]

            canvas.drawRect(currentX, startY, currentX + colWidth, startY + rowHeight, backgroundPaint)
            canvas.drawRect(currentX, startY, currentX + colWidth, startY + rowHeight, borderPaint)

            val textX = currentX + (colWidth / 2)
            val textY = startY + (rowHeight / 2) + (textPaint.textSize / 3)

            drawCellText(canvas, text, textX, textY, colWidth, textPaint)
            currentX += colWidth
        }

        return startY + rowHeight
    }

    private fun drawCellText(
        canvas: Canvas,
        text: String,
        centerX: Float,
        centerY: Float,
        maxWidth: Float,
        paint: Paint
    ) {
        val availableWidth = maxWidth - (2 * config.cellPadding)
        if (paint.measureText(text) <= availableWidth) {
            canvas.drawText(text, centerX, centerY, paint)
        } else {
            var truncated = text
            while (paint.measureText("$truncated...") > availableWidth && truncated.isNotEmpty()) {
                truncated = truncated.dropLast(1)
            }
            canvas.drawText("$truncated...", centerX, centerY, paint)
        }
    }

    private fun calculateRowHeight(): Float {
        return config.textSize + (2 * config.cellPadding) + 10f
    }
}
