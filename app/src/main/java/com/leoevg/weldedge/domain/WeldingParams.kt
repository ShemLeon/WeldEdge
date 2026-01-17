package com.leoevg.weldedge.domain

data class WeldingParams(
    val metalType: String = "нержавейка",
    val thickness: String = "",
    val jointType: String = "стык",
    val responsibility: String = "нагруженный",
    val engineerName: String = "",
    val standard: String = "ГОСТ"
)
