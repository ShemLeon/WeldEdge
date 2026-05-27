package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JointType(
    @SerialName("id") val id: String,
    @SerialName("name_res") val nameRes: String,
    @SerialName("icon_res") val iconRes: String,
    @SerialName("name_en") val nameEn: String = ""
)