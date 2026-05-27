package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdgePreparationItem(
    val id: String,
    @SerialName("image_path") val imagePath: String,
    val name: String
)