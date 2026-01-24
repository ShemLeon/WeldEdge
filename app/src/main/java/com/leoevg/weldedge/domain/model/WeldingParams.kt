package com.leoevg.weldedge.domain.model

data class WeldingParams(
    val metalType: String = "нержавейка",
    val thickness: String = "",
    val jointType: String = "стык",
    val responsibility: String = "нагруженный",
    val edgePreparation: String = "",
    val engineerName: String = "",
    val standard: String = "ГОСТ"
)
