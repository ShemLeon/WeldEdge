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
    val color: String,
    val subcategories: List<MetalSubcategory> = emptyList()
) {
    // For backward compatibility while migrating UI
    val markMetal: List<String>
        get() = subcategories.flatMap { sub -> sub.marks.map { it.id } }
}

@Serializable
data class MetalSubcategory(
    val id: String,
    val name: String,
    val marks: List<MetalMark> = emptyList()
)

@Serializable
data class MetalMark(
    val id: String,
    @SerialName("intl_classification")
    val intlClassification: String,
    @SerialName("ru_designation")
    val ruDesignation: String,
    @SerialName("chemical_content")
    val chemicalContent: String
)
