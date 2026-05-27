package com.leoevg.weldedge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlloysDatabase(
    val metals1: List<MetalGroup> = emptyList(),
    val thickness: List<Double> = emptyList(),
    @SerialName("edge_preparation") val edgePreparation: List<EdgePreparationGroup> = emptyList(),
    @SerialName("welding_type") val weldingType: List<WeldingTypeItem> = emptyList()
) {
    companion object {
        const val ASSET_PATH = "data/alloys_database.json"
    }

    fun getMetalGroupById(id: String): MetalGroup? = metals1.find { it.id == id }

    fun getAllGrades(): List<Pair<MetalGroup, String>> =
        metals1.flatMap { group -> group.markMetal.map { grade -> group to grade } }

    fun getMetalGroupForGrade(gradeName: String): MetalGroup? =
        metals1.find { group -> group.markMetal.any { it.equals(gradeName, ignoreCase = true) } }

    /**
     * Finds detailed information about a specific metal grade.
     */
    fun findMarkById(markId: String): MetalMark? {
        return metals1.asSequence()
            .flatMap { it.subcategories }
            .flatMap { it.marks }
            .find { it.id.equals(markId, ignoreCase = true) }
    }

    // Resolves legacy aliases (e.g. "316L" → "AISI 316L", "15-5 PH" → "AISI 630 / 15-5 PH").
    // Returns the canonical grade name from the database, or null if not found.
    fun findGradeByName(name: String): String? {
        val found = getAllGrades().find { it.second.equals(name, ignoreCase = true) }?.second
        if (found != null) return found
        val alt = when (name.trim()) {
            "15-5 PH", "AISI 15-5 PH" -> "AISI 630 / 15-5 PH"
            "17-4 PH", "AISI 17-4 PH" -> "AISI 630 / 17-4 PH"
            else -> "AISI ${name.removePrefix("AISI").removePrefix(" ").trim()}"
        }
        return getAllGrades().find { it.second.equals(alt, ignoreCase = true) }?.second
    }
}