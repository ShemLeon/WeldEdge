package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Элемент разделки кромок из JSON (bw_preparation / fw_preparation).
 */
@Serializable
data class EdgePreparationItem(
    val id: String,
    val image_path: String,
    val name: String
)
