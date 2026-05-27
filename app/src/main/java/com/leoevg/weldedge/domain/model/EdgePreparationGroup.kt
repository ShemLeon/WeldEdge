package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdgePreparationGroup(
    val id: String,
    @SerialName("name_en") val nameEn: String = "",
    @SerialName("icon_res") val iconRes: String = "",
    val subcategories: List<EdgePreparationSubcategory> = emptyList()
)