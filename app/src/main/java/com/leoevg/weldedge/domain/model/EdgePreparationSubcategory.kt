package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdgePreparationSubcategory(
    val id: String,
    @SerialName("name_en") val nameEn: String = "",
    val items: List<EdgePreparationItem> = emptyList()
)