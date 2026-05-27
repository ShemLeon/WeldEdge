package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeldingTypeItem(
    val id: String,
    @SerialName("image_path") val imagePath: String,
    val name: String,
    @SerialName("process_code") val processCode: String = ""
)