package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Конфигурация разделок кромок: BW (stress) и FW (simple).
 */
@Serializable
data class EdgePreparationData(
    @SerialName("bw_preparation") val bwPreparation: List<EdgePreparationItem> = emptyList(),
    @SerialName("fw_preparation") val fwPreparation: List<EdgePreparationItem> = emptyList()
)
