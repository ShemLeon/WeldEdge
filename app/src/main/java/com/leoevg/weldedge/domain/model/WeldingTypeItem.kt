package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Элемент типа сварки из JSON (welding_type).
 */
@Serializable
data class WeldingTypeItem(
    val id: String,
    val image_path: String,
    val name: String
)
