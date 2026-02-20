package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Подгруппа сплавов (ISO 15608 группа или серия).
 */
@Serializable
data class AlloySubgroup(
    val id: String,
    val name: String,
    val isoGroup: String? = null,
    val grades: List<AlloyGrade> = emptyList()
)
