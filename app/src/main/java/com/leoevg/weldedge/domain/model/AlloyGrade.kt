package com.leoevg.weldedge.domain.model

import kotlinx.serialization.Serializable

/**
 * Марка сплава в базе данных.
 * @param name — основное обозначение (AISI, EN, AA)
 * @param chemicalComposition — химсостав (строка)
 * @param awsName — обозначение по AWS
 * @param gostName — обозначение по ГОСТ
 * @param preheating — подогрев: "-" или температура в °C
 */
@Serializable
data class AlloyGrade(
    val name: String,
    val chemicalComposition: String,
    val awsName: String,
    val gostName: String,
    val preheating: String
)
