package com.leoevg.weldedge.domain

import android.graphics.Paint

data class Table(
    val columns: Int,
    val rows: Int,
    val data: List<List<String>>, // rows x columns
    val headerTitle: String? = null,
    val headerRow: List<String>? = null,
    val footerTitle: String? = null,
    val columnWeights: List<Float>? = null,
    val columnAligns: List<Paint.Align>? = null,
    val config: TableConfig = TableConfig()
) {
    init {
        require(columns > 0) { "Table must have at least 1 column" }
        require(rows > 0) { "Table must have at least 1 row" }
        require(data.size == rows) { "Data must have $rows rows" }
        require(data.all { it.size == columns }) { "All rows must have $columns columns" }
        headerRow?.let {
            require(it.size == columns) { "Header row must have $columns columns" }
        }
        columnWeights?.let {
            require(it.size == columns) { "Column weights must match column count" }
        }
        columnAligns?.let {
            require(it.size == columns) { "Column aligns must match column count" }
        }
    }
}
