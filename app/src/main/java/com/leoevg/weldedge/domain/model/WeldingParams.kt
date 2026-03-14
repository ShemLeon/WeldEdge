package com.leoevg.weldedge.domain.model

data class BeAvailableWeldingParams(
    val metalType: List<String> = listOf(),
    val metalType2: List<String> = listOf(),
    val thickness: List<String> = listOf(),
    val jointType: List<String> = listOf(),
    val typeOfWeld: List<String> = listOf(),
    val edgePreparation: List<String> = listOf(),
    val weldingType: List<String> = listOf(),
    val engineerName: List<String> = listOf(), // пока не актуально
    val standard: List<String> = listOf()
)
data class WeldingParams(
    val metalType: String = "",
    val metalType2: String = "",
    val thickness: String = "",
    val jointType: String = "",
    val typeOfWeld: String = "",
    val edgePreparation: String = "",
    val weldingType: String = "",
    val engineerName: String = "", // пока не актуально
    val standard: String = ""
) {
    // Функция для расчета номера WPS по таблице
    fun getWPSnumber(): String {
        val thicknessVal = thickness.toDoubleOrNull() ?: return "_________"
        val process = getProcess()
        if (process == "_________") return "_________"

        // Определяем категорию каждого металла
        val cat1 = Alloys.findByName(metalType)?.category?.displayName ?: return "_________"
        val cat2 = Alloys.findByName(metalType2)?.category?.displayName ?: return "_________"

        val entry = WpsTable.findWps(cat1, cat2, metalType, metalType2, process, typeOfWeld, jointType, thicknessVal)
        return entry?.wpsNumber?.ifEmpty { "_________" } ?: "_________"
    }

    // Функция для расчета Root Opening
    fun getRootOpening(): String {
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0
        
        val value = if (typeOfWeld == "FW" && thicknessVal <= 2.5) {
            "1.1"
        } else {
            "0.76"
        }
        return "≤ $value"
    }

    // Функция для расчета Root Face
    fun getRootFace(): String {
        if (typeOfWeld == "FW") {
            return "-"
        }

        val process = getProcess()
        val edgePrep = EdgePreparation.fromId(edgePreparation)
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0

        return when {
            // Single Bevel (все типы соединений)
            edgePrep == EdgePreparation.GROOVE_BEVEL_SINGLE || 
            edgePrep == EdgePreparation.T_BEVEL_SINGLE || 
            edgePrep == EdgePreparation.T_BEVEL_SUPPORT || 
            edgePrep == EdgePreparation.LAP_BEVEL || 
            edgePrep == EdgePreparation.CORNER_BEVEL_INSIDE || 
            edgePrep == EdgePreparation.CORNER_BEVEL_OUTSIDE -> "0.76-1.52"
            
            // Single J (все типы соединений)
            edgePrep == EdgePreparation.GROOVE_J_SINGLE || 
            edgePrep == EdgePreparation.T_J_GROOVE_SINGLE ||
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
            edgePrep == EdgePreparation.GROOVE_J_DOUBLE ||
            edgePrep == EdgePreparation.T_J_GROOVE_DOUBLE -> when (process) {
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

    // Функция для расчета Groove Angle
    fun getGrooveAngle(): String {
        if (typeOfWeld == "FW") {
            return "-"
        }

        val process = getProcess()
        val edgePrep = EdgePreparation.fromId(edgePreparation)
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0
        val alloy = Alloys.findByName(metalType)
        val isAluOrTi = alloy?.category == AlloyCategory.ALUMINIUM || alloy?.category?.displayName == "Ti"

        return when {
            // Bevel и V (все типы соединений, single и double)
            edgePrep == EdgePreparation.GROOVE_BEVEL_SINGLE || 
            edgePrep == EdgePreparation.T_BEVEL_SINGLE || 
            edgePrep == EdgePreparation.T_BEVEL_SUPPORT || 
            edgePrep == EdgePreparation.LAP_BEVEL || 
            edgePrep == EdgePreparation.CORNER_BEVEL_INSIDE || 
            edgePrep == EdgePreparation.CORNER_BEVEL_OUTSIDE ||
            edgePrep == EdgePreparation.GROOVE_V_SINGLE || 
            edgePrep == EdgePreparation.CORNER_V_GROOVE ||
            edgePrep == EdgePreparation.GROOVE_BEVEL_DOUBLE || 
            edgePrep == EdgePreparation.T_BEVEL_DOUBLE ||
            edgePrep == EdgePreparation.GROOVE_V_DOUBLE -> {
                when (process) {
                    "GTAW" -> if (thicknessVal in 1.5..13.0) "60°" else "_________"
                    "GMAW", "SAW" -> if (isAluOrTi) "60°" else "45°"
                    else -> "_________"
                }
            }
            // Все U-Groove
            edgePrep == EdgePreparation.GROOVE_U_SINGLE || 
            edgePrep == EdgePreparation.GROOVE_U_DOUBLE || 
            edgePrep == EdgePreparation.CORNER_U -> "20°"
            
            // Все J-Groove
            edgePrep == EdgePreparation.GROOVE_J_SINGLE || 
            edgePrep == EdgePreparation.GROOVE_J_DOUBLE || 
            edgePrep == EdgePreparation.T_J_GROOVE_SINGLE ||
            edgePrep == EdgePreparation.T_J_GROOVE_DOUBLE ||
            edgePrep == EdgePreparation.CORNER_J_INSIDE || 
            edgePrep == EdgePreparation.CORNER_J_OUTSIDE -> "30°"

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

    fun getEnglishTypeOfWeld(): String {
        return when (typeOfWeld) {
            "BW" -> "BW"
            "FW" -> "FW"
            else -> typeOfWeld
        }
    }

    fun getEnglishMetalType(): String {
        return Alloys.findByName(metalType)?.getFullName() ?: metalType
    }

    fun getEnglishMetalType2(): String {
        return Alloys.findByName(metalType2)?.getFullName() ?: metalType2
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
