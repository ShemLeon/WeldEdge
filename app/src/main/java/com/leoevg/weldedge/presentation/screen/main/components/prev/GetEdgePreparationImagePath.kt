package com.leoevg.weldedge.presentation.screen.main.components.prev

import androidx.compose.runtime.Composable

@Composable
fun getEdgePreparationImagePath(
    jointType: String,
    responsibility: String,
    edgePreparation: String
): String? {
    if (edgePreparation.isEmpty()) return null

    val folder = when (jointType) {
        "butt" -> "groove"
        "t_joint" -> "t"
        "lap" -> "lap"
        "corner" -> "corner"
        else -> "groove"
    }
    val subFolder = if (responsibility == "stress") "stress" else "simple"
    val fullPath = "edge_preparation/$folder/$subFolder"

    return "file:///android_asset/$fullPath/$edgePreparation"
}