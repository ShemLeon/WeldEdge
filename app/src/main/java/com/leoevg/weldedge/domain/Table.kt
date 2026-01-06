package com.leoevg.weldedge.domain

data class Table(
    val columns: Int,
    val rows: Int,
    val data: List<List<String>>, // rows x columns
    val headerTitle: String? = null,
    val footerTitle: String? = null,
    val config: TableConfig = TableConfig()
) {
    init {
        require(columns > 0) { "Table must have at least 1 column" }
        require(rows > 0) { "Table must have at least 1 row" }
        require(data.size == rows) { "Data must have $rows rows" }
        require(data.all { it.size == columns }) { "All rows must have $columns columns" }
    }
}

