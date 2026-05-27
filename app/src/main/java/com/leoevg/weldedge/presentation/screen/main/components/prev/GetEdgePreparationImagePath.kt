package com.leoevg.weldedge.presentation.screen.main.components.prev

import com.leoevg.weldedge.domain.model.EdgePreparationItem

fun getEdgePreparationImagePath(item: EdgePreparationItem?): String? =
    item?.imagePath?.let { "file:///android_asset/$it" }