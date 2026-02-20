package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Корневая структура JSON-базы сплавов.
 */
@Serializable
data class AlloysDatabase(
    val version: Int = 1,
    val groups: List<AlloyGroup> = emptyList()
) {
    companion object {
        const val ASSET_PATH = "data/alloys_database.json"
    }

    fun getAllGrades(): List<Triple<AlloyGroup, AlloySubgroup, AlloyGrade>> =
        groups.flatMap { group ->
            group.subgroups.flatMap { subgroup ->
                subgroup.grades.map { grade ->
                    Triple(group, subgroup, grade)
                }
            }
        }

    fun findGradeByName(name: String): AlloyGrade? {
        val found = getAllGrades().find { it.third.name.equals(name, ignoreCase = true) }?.third
        if (found != null) return found
        val alt = when (name.trim()) {
            "15-5 PH", "AISI 15-5 PH" -> "AISI 630 / 15-5 PH"
            "17-4 PH", "AISI 17-4 PH" -> "AISI 630 / 17-4 PH"
            else -> if (name.startsWith("AISI ", ignoreCase = true)) name else "AISI $name"
        }
        return getAllGrades().find { it.third.name.equals(alt, ignoreCase = true) }?.third
    }

    fun getGroupById(id: String): AlloyGroup? = groups.find { it.id == id }
}
