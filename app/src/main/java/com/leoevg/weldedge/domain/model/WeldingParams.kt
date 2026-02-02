package com.leoevg.weldedge.domain.model

data class WeldingParams(
    val metalType: String = "",
    val thickness: String = "",
    val jointType: String = "",
    val responsibility: String = "",
    val edgePreparation: String = "",
    val weldingType: String = "",
    val engineerName: String = "",
    val standard: String = ""
)
