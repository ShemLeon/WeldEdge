package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Корневая структура JSON (alloys_database.json / all_params).
 * Соответствует структуре: metals1, thickness, joint_type, edge_preparation, welding_type.
 */
@Serializable
data class AlloysDatabase(
    val metals1: List<MetalGroup> = emptyList(),
    val thickness: List<Double> = emptyList(),
    @SerialName("joint_type") val jointType: List<JointType> = emptyList(),
    @SerialName("edge_preparation") val edgePreparation: List<EdgePreparationGroup> = emptyList(),
    @SerialName("welding_type") val weldingType: List<WeldingTypeItem> = emptyList()
) {
    companion object {
        const val ASSET_PATH = "data/alloys_database.json"
    }

    fun getMetalGroupById(id: String): MetalGroup? = metals1.find { it.id == id }

    fun getAllGrades(): List<Pair<MetalGroup, String>> =
        metals1.flatMap { group ->
            group.mark_metal.map { gradeName -> group to gradeName }
        }

    fun findGradeByName(name: String): String? {
        val found = getAllGrades().find { it.second.equals(name, ignoreCase = true) }?.second
        if (found != null) return found
        val alt = when (name.trim()) {
            "15-5 PH", "AISI 15-5 PH" -> "AISI 630 / 15-5 PH"
            "17-4 PH", "AISI 17-4 PH" -> "AISI 630 / 17-4 PH"
            else -> if (name.startsWith("AISI ", ignoreCase = true)) name else "AISI $name"
        }
        return getAllGrades().find { it.second.equals(alt, ignoreCase = true) }?.second
    }

    fun getMetalGroupForGrade(gradeName: String): MetalGroup? =
        metals1.find { group -> group.mark_metal.any { it.equals(gradeName, ignoreCase = true) } }
}

// TODO: надо из алойсдатабейс сделать беавалаблпарамс.