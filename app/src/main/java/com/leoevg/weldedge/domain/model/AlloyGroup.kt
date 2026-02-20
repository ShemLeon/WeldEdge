package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Главная группа сплавов: AL, CS, SS.
 */
@Serializable
data class AlloyGroup(
    val id: String,
    val name: String,
    val subgroups: List<AlloySubgroup> = emptyList()
)
