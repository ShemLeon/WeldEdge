package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Группа металлов из all_params / alloys_database (metals1).
 */
@Serializable
data class MetalGroup(
    val id: String,
    val name: String,
    val mark_metal: List<String> = emptyList()
)
