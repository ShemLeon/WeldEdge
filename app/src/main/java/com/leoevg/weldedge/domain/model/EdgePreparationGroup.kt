package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Группа разделок кромок из JSON (bw_preparation / fw_preparation).
 * Элемент массива edge_preparation.
 */
@Serializable
data class EdgePreparationGroup(
    val id: String,
    @SerialName("name_res") val nameRes: String = "",
    val array: List<EdgePreparationItem> = emptyList()
)
