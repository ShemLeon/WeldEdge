package com.leoevg.weldedge.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.leoevg.weldedge.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JointType(
    @SerialName("id") val id: String,
    @SerialName("name_res") val nameRes: String,
    @SerialName("icon_res") val iconRes: String
)