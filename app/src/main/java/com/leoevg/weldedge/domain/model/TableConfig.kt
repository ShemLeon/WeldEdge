package com.leoevg.weldedge.domain.model

import android.graphics.Color

data class TableConfig(
    val cellPaddingHorizontal: Float = 4f,
    val cellPaddingVertical: Float = 6f,
    val borderWidth: Float = 0.8f,
    val borderColor: Int = Color.BLACK,
    val headerBackgroundColor: Int = Color.LTGRAY,
    val headerTextColor: Int = Color.BLACK,
    val cellTextColor: Int = Color.BLACK,
    val alternateRowColor: Int? = null,
    val textSize: Float = 10f,
    val headerTextSize: Float = 10f,
    val headerBold: Boolean = true
)
