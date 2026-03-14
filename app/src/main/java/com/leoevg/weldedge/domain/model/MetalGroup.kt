package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Группа металлов из all_params / alloys_database (metals1).
 */
@Serializable
data class MetalGroup(
    val id: String,
    val name: String,
    @SerialName("mark_metal")
    val markMetal: List<String> = emptyList(),
    val color: String
)
