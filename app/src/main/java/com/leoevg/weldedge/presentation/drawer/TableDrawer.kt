package com.leoevg.weldedge.presentation.drawer

import android.graphics.*
import android.graphics.pdf.PdfDocument
import com.leoevg.weldedge.domain.Table
import com.leoevg.weldedge.domain.TableConfig

class TableDrawer(private val config: TableConfig = TableConfig()) {

    /**
     * Draw table at specified position
     * Returns the Y position after the table
     */
    fun drawTable(
        canvas: Canvas,
        table: Table,
        startX: Float,
        startY: Float,
        maxWidth: Float,
        maxHeight: Float = Float.MAX_VALUE
    ): Float {
        val colWidth = maxWidth / table.columns

        val rowHeight = calculateRowHeight()
        var currentY = startY

        // Draw header if exists
        table.headerTitle?.let { header ->
            currentY = drawHeaderFooterRow(canvas, header, startX, currentY, maxWidth, rowHeight)
        }

        // Draw data rows
        table.data.forEachIndexed { rowIndex, rowData ->
            if (currentY + rowHeight > maxHeight) {
                return currentY // Stop if exceeds max height
            }

            currentY = drawDataRow(
                canvas,
                rowData,
                rowIndex,
                startX,
                currentY,
                colWidth,
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
        colWidth: Float,
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

        // Draw background
        canvas.drawRect(
            startX,
            startY,
            startX + colWidth,
            startY + rowHeight,
            backgroundPaint
        )

        // Draw border
        canvas.drawRect(
            startX,
            startY,
            startX + colWidth,
            startY + rowHeight,
            borderPaint
        )

        // Draw text (centered)
        val textX = startX + (colWidth / 2)
        val textY = startY + (rowHeight / 2) + (config.headerTextSize / 3)

        // Handle text wrapping if needed
        drawCellText(canvas, title, textX, textY, colWidth, textPaint)

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

        // Simple text drawing - can be enhanced with wrapping
        if (paint.measureText(text) <= availableWidth) {
            canvas.drawText(text, centerX, centerY, paint)
        } else {
            // Truncate with ellipsis
            var truncated = text
            while (paint.measureText("$truncated...") > availableWidth && truncated.isNotEmpty()) {
                truncated = truncated.dropLast(1)
            }
            canvas.drawText("$truncated...", centerX, centerY, paint)
        }
    }

    private fun drawDataRow(
        canvas: Canvas,
        rowData: List<String>,
        rowIndex: Int,
        startX: Float,
        startY: Float,
        colWidth: Float,
        rowHeight: Float
    ): Float {
        val textPaint = Paint().apply {
            textSize = config.textSize
            color = config.cellTextColor
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val backgroundPaint = Paint().apply {
            color = if (rowIndex % 2 == 1 && config.alternateRowColor != null) {
                config.alternateRowColor
            } else {
                Color.WHITE
            }
            style = Paint.Style.FILL
        }

        val borderPaint = Paint().apply {
            color = config.borderColor
            strokeWidth = config.borderWidth
            style = Paint.Style.STROKE
        }

        rowData.forEachIndexed { colIndex, text ->
            val cellX = startX + (colIndex * colWidth)

            // Draw background
            canvas.drawRect(
                cellX,
                startY,
                cellX + colWidth,
                startY + rowHeight,
                backgroundPaint
            )

            // Draw border
            canvas.drawRect(
                cellX,
                startY,
                cellX + colWidth,
                startY + rowHeight,
                borderPaint
            )

            // Draw text (centered)
            val textX = cellX + (colWidth / 2)
            val textY = startY + (rowHeight / 2) + (config.textSize / 3)

            drawCellText(canvas, text, textX, textY, colWidth, textPaint)
        }

        return startY + rowHeight
    }

    private fun calculateRowHeight(): Float {
        return config.textSize + (2 * config.cellPadding) + 10f
    }
}