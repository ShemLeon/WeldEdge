package com.leoevg.weldedge.domain

import android.graphics.Color

data class TableConfig(
    val cellPadding: Float = 8f,
    val borderWidth: Float = 1f,
    val borderColor: Int = Color.BLACK,
    val headerBackgroundColor: Int = Color.LTGRAY,
    val headerTextColor: Int = Color.BLACK,
    val cellTextColor: Int = Color.BLACK,
    val alternateRowColor: Int? = Color.parseColor("#F5F5F5"), // Light gray
    val textSize: Float = 12f,
    val headerTextSize: Float = 14f,
    val headerBold: Boolean = true
)
