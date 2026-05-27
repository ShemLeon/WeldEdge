package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdgePreparationGroup(
    val id: String,
    @SerialName("name_res") val nameRes: String = "",
    @SerialName("name_en") val nameEn: String = "",
    val array: List<EdgePreparationItem> = emptyList()
)