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

    // Функция для расчета Root Opening
    fun getRootOpening(): String {
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0
        
        val value = if (responsibility == "simple" && thicknessVal <= 2.5) {
            "1.1"
        } else {
            "0.76"
        }
        return "≤ $value"
    }

    // Функция для расчета Root Face
    fun getRootFace(): String {
        val process = getProcess()
        val edgePrep = EdgePreparation.fromId(edgePreparation)

        // Для Single Bevel Groove особые условия
        if (edgePrep == EdgePreparation.GROOVE_BEVEL_SINGLE) {
            val value = if (process == "GTAW") "1.5" else "2.3"
            return "≤ $value"
        }

        // Базовое условие для швов без разделки
        val value = if (responsibility == "simple") {
            "0.76"
        } else {
            "_________"
        }

        return if (value.contains("_")) value else "≤ $value"
    }

    // Функция для получения рекомендованной проволоки
    fun getRecommendedWire(): String {
        return Wires.getRecommendedWire(metalType)?.name ?: "_________"
    }

    // Функция для получения номера классификации по AWS
    fun getAwsClassification(): String {
        return Alloys.findByName(metalType)?.getEffectiveAwsClassification() ?: "_________"
    }

    // Функция для получения номера стандарта
    fun getWPSnumber(): String {
        return when (jointType) {
            "butt" -> "3092 (013)"
            else -> "_________"
        }
    }

    // Функции для получения типа сварки в отчете (Process)
    fun getProcess(): String {
        return WeldingType.fromId(weldingType)?.processName ?: "_________"
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
        return Alloys.findByName(metalType)?.getFullName() ?: metalType
    }

    fun getEnglishStandard(): String {
        return when (standard) {
            "ГОСТ" -> "GOST"
            else -> standard
        }
    }

    fun getEdgePreparationFullPath(): String {
        return EdgePreparation.fromId(edgePreparation)?.getAssetPath() ?: ""
    }
}
