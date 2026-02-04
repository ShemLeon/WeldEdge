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
) {

    // Функции для получения номера стандарта
    fun getWPSnumber(): String {
        return when (jointType) {
            "butt" -> "3092 (013)"
            else -> "_________"
        }
    }

    // Функции для получения типа сварки в отчете (Process)
    fun getProcess(): String {
        return when (weldingType) {
            "TIG" -> "GTAW"
            "MIGMAG" -> "GMAW"
            "MMA" -> "SMAW"
            else -> "_________"
        }
    }

    // Функции для получения преобразованных значений
    fun getEnglishJointType(): String {
        return when (jointType) {
            "butt" -> "Butt"
            "t_joint" -> "T-joint"
            "corner" -> "Corner"
            "lap" -> "Lap"
            else -> jointType.replaceFirstChar { it.uppercase() }
        }
    }

    fun getEnglishResponsibility(): String {
        return when (responsibility) {
            "stress" -> "With groove"
            "simple" -> "Without groove"
            else -> responsibility
        }
    }

    fun getEnglishMetalType(): String {
        return when (metalType) {
            "нержавейка" -> "Stainless Steel"
            "углеродистая сталь" -> "Carbon Steel"
            "алюминий" -> "Aluminum"
            else -> metalType
        }
    }

    fun getEnglishStandard(): String {
        return when (standard) {
            "ГОСТ" -> "GOST"
            else -> standard
        }
    }

    fun getEdgePreparationFolder(): String {
        return when (jointType) {
            "butt" -> "groove"
            "t_joint" -> "t"
            "lap" -> "lap"
            "corner" -> "corner"
            else -> "groove"
        }
    }

    fun getEdgePreparationSubFolder(): String {
        return if (responsibility == "stress") "stress" else "simple"
    }

    fun getEdgePreparationFullPath(): String {
        return if (edgePreparation.isNotEmpty()) {
            "edge_preparation/${getEdgePreparationFolder()}/${getEdgePreparationSubFolder()}/$edgePreparation"
        } else {
            ""
        }
    }
}