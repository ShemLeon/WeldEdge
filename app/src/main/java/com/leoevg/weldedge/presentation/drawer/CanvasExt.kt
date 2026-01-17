package com.leoevg.weldedge.presentation.drawer

import android.graphics.Canvas
import com.leoevg.weldedge.domain.model.Table
import com.leoevg.weldedge.domain.model.TableConfig

fun Canvas.drawSimpleTable(
    data: List<List<String>>,
    headerTitle: String? = null,
    footerTitle: String? = null,
    columnWeights: List<Float>? = null,
    x: Float,
    y: Float,
    width: Float,
    config: TableConfig = TableConfig()
): Float {
    val table = Table(
        columns = data.firstOrNull()?.size ?: 0,
        rows = data.size,
        data = data,
        headerTitle = headerTitle,
        footerTitle = footerTitle,
        columnWeights = columnWeights,
        config = config
    )

    return TableDrawer(config).drawTable(this, table, x, y, width)
}
