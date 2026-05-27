package com.leoevg.weldedge.domain.model

data class BeAvailableWeldingParams(
    val metalType: List<MetalGroup> = listOf(),
    val markMetal: List<String> = listOf(),
    val thickness: List<String> = listOf(),
    val typeOfWeld: List<EdgePreparationGroup> = listOf(),
    val edgePreparation: List<EdgePreparationItem> = listOf(),
    val weldingType: List<WeldingTypeItem> = listOf(),
    val engineerName: List<String> = listOf(),
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
    val engineerName: String = "",
    val standard: String = ""
) {
    fun getRootOpening(): String {
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0
        val value = if (typeOfWeld == "FW" && thicknessVal <= 2.5) "1.1" else "0.76"
        return "≤ $value"
    }

    fun getProcess(database: AlloysDatabase): String =
        database.weldingType.find { it.id == weldingType }?.processCode ?: "_________"

    fun getEnglishJointType(database: AlloysDatabase): String =
        database.edgePreparation.find { it.id == jointType }?.nameEn
            ?: jointType.replaceFirstChar { it.uppercase() }

    fun getEnglishTypeOfWeld(): String = typeOfWeld

    fun getEnglishMetalType(): String = metalType

    fun getEnglishMetalType2(): String = metalType2

    fun getEnglishStandard(): String = if (standard == "ГОСТ") "GOST" else standard

    fun getEdgePreparationFullPath(database: AlloysDatabase): String =
        database.edgePreparation
            .flatMap { it.subcategories }
            .flatMap { it.items }
            .find { it.id == edgePreparation }?.imagePath ?: ""

    // AWS classification and wire recommendations are not yet in the database.
    fun getAwsClassification(): String = "_________"
    fun getRecommendedWire(): String = "_________"

    fun getWPSnumber(database: AlloysDatabase): String {
        val thicknessVal = thickness.toDoubleOrNull() ?: return "_________"
        val process = getProcess(database)
        if (process == "_________") return "_________"
        val group1 = database.getMetalGroupForGrade(metalType) ?: return "_________"
        val group2 = database.getMetalGroupForGrade(metalType2) ?: return "_________"
        val entry = WpsTable.findWps(
            group1.id, group2.id, metalType, metalType2,
            process, typeOfWeld, jointType, thicknessVal
        )
        return entry?.wpsNumber?.ifEmpty { "_________" } ?: "_________"
    }

    fun getRootFace(database: AlloysDatabase): String {
        if (typeOfWeld == "FW") return "-"
        val process = getProcess(database)
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0

        return when {
            edgePreparation in setOf(
                "groove_bevel.webp", "t_joint_bevel.webp", "t_joint_bevel_support.webp",
                "lap_joint_bevel.webp", "corner_bevel_inside.webp", "corner_bevel_outside.webp"
            ) -> "0.76-1.52"

            edgePreparation in setOf(
                "groove_j.webp", "t_joint_j_groove.webp",
                "corner_j_inside.webp", "corner_j_outside.webp"
            ) -> when (process) {
                "GTAW" -> "0.76-1.52"
                else -> "_________"
            }

            edgePreparation in setOf("groove_v_single.webp", "corner_v_groove.webp") -> when (process) {
                "GTAW" -> "0.76-1.52"
                "GMAW", "SAW" -> "1.52-2.28"
                else -> "_________"
            }

            edgePreparation in setOf(
                "groove_bevel_double.webp", "t_joint_bevel_double.webp", "groove_v_double.webp"
            ) -> when (process) {
                "GTAW" -> "0.51-1.52"
                "GMAW", "SAW" -> "1.25-2.26"
                else -> "_________"
            }

            edgePreparation in setOf(
                "groove_u_double.webp", "groove_j_double.webp", "t_joint_j_groove_double.webp"
            ) -> when (process) {
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

            edgePreparation in setOf("groove_u.webp", "corner_u.webp") -> when (process) {
                "GTAW" -> "0.76-1.52"
                "GMAW", "SAW" -> "1.25-2.26"
                else -> "_________"
            }

            else -> "_________"
        }
    }

    fun getGrooveAngle(database: AlloysDatabase): String {
        if (typeOfWeld == "FW") return "-"
        val process = getProcess(database)
        val thicknessVal = thickness.toDoubleOrNull() ?: 0.0
        val isAlu = database.getMetalGroupForGrade(metalType)?.id == "AL"

        return when {
            edgePreparation in setOf(
                "groove_bevel.webp", "t_joint_bevel.webp", "t_joint_bevel_support.webp",
                "lap_joint_bevel.webp", "corner_bevel_inside.webp", "corner_bevel_outside.webp",
                "groove_v_single.webp", "corner_v_groove.webp",
                "groove_bevel_double.webp", "t_joint_bevel_double.webp", "groove_v_double.webp"
            ) -> when (process) {
                "GTAW" -> if (thicknessVal in 1.5..13.0) "60°" else "_________"
                "GMAW", "SAW" -> if (isAlu) "60°" else "45°"
                else -> "_________"
            }

            edgePreparation in setOf(
                "groove_u.webp", "groove_u_double.webp", "corner_u.webp"
            ) -> "20°"

            edgePreparation in setOf(
                "groove_j.webp", "groove_j_double.webp",
                "t_joint_j_groove.webp", "t_joint_j_groove_double.webp",
                "corner_j_inside.webp", "corner_j_outside.webp"
            ) -> "30°"

            else -> "_________"
        }
    }
}