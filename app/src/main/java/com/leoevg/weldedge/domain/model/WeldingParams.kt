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
        if (responsibility == "simple") {
            return "X"
        }

        val process = getProcess()
        val edgePrep = EdgePreparation.fromId(edgePreparation)
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0

        return when {
            // Single Bevel (все типы соединений)
            edgePrep == EdgePreparation.GROOVE_BEVEL_SINGLE || 
            edgePrep == EdgePreparation.T_BEVEL_SINGLE || 
            edgePrep == EdgePreparation.LAP_BEVEL || 
            edgePrep == EdgePreparation.CORNER_BEVEL_INSIDE || 
            edgePrep == EdgePreparation.CORNER_BEVEL_OUTSIDE -> "0.76-1.52"
            
            // Single J (все типы соединений)
            edgePrep == EdgePreparation.GROOVE_J_SINGLE || 
            edgePrep == EdgePreparation.CORNER_J_INSIDE || 
            edgePrep == EdgePreparation.CORNER_J_OUTSIDE -> when (process) {
                "GTAW" -> "0.76-1.52"
                else -> "_________"
            }
            
            // Single V (все типы соединений)
            edgePrep == EdgePreparation.GROOVE_V_SINGLE || 
            edgePrep == EdgePreparation.CORNER_V_GROOVE -> when (process) {
                "GTAW" -> "0.76-1.52"
                "GMAW", "SAW" -> "1.52-2.28"
                else -> "_________"
            }

            // Double Bevel и Double V (все типы соединений)
            edgePrep == EdgePreparation.GROOVE_BEVEL_DOUBLE || 
            edgePrep == EdgePreparation.T_BEVEL_DOUBLE ||
            edgePrep == EdgePreparation.GROOVE_V_DOUBLE -> when (process) {
                "GTAW" -> "0.51-1.52"
                "GMAW", "SAW" -> "1.25-2.26"
                else -> "_________"
            }

            // Double U и Double J
            edgePrep == EdgePreparation.GROOVE_U_DOUBLE || 
            edgePrep == EdgePreparation.GROOVE_J_DOUBLE -> when (process) {
                "GTAW" -> when {
                    thicknessVal in 13.1..20.0 -> "0.75-1.76"
                    thicknessVal > 20.0 -> "1.25-2.26"
                    else -> "_________"
                }
                "GMAW", "SAW" -> when {
                    thicknessVal in 13.1..20.0 -> "1.75-2.76"
                    thicknessVal > 20.0 -> "2.75-3.76"
                    else -> "_________"
                }
                else -> "_________"
            }

            // Single U (стыковые и угловые)
            edgePrep == EdgePreparation.GROOVE_U_SINGLE || 
            edgePrep == EdgePreparation.CORNER_U -> when (process) {
                "GTAW" -> "0.76-1.52"
                "GMAW", "SAW" -> "1.25-2.26"
                else -> "_________"
            }

            else -> "_________"
        }
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
