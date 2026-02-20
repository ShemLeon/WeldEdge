package com.leoevg.weldedge.domain.model

enum class EdgePreparation(
    val id: String,
    val displayName: String,
    val assetFileName: String,
    val jointType: String,
    val typeOfWelds: TypeOfWelds
) {
    // GROOVE SIMPLE (FW now)
    GROOVE_SQUARE_SINGLE("groove_1_square_single.webp", "Square single", "groove_1_square_single.webp", "butt", TypeOfWelds.FW),
    GROOVE_SQUARE_DOUBLE("groove_2_square_double.webp", "Square double", "groove_2_square_double.webp", "butt", TypeOfWelds.FW),
    GROOVE_FLARE_V_SINGLE("groove_3_flare_v.webp", "Flare-V single", "groove_3_flare_v.webp", "butt", TypeOfWelds.FW),
    GROOVE_FLARE_V_DOUBLE("groove_4_flare_v_double.webp", "Flare-V double", "groove_4_flare_v_double.webp", "butt", TypeOfWelds.FW),

    // GROOVE STRESS (BW now)
    GROOVE_V_SINGLE("groove_v_single.webp", "V single", "groove_v_single.webp", "butt", TypeOfWelds.BW),
    GROOVE_V_DOUBLE("groove_v_double.webp", "V double", "groove_v_double.webp", "butt", TypeOfWelds.BW),
    GROOVE_BEVEL_SINGLE("groove_bevel.webp", "Bevel single", "groove_bevel.webp", "butt", TypeOfWelds.BW),
    GROOVE_BEVEL_DOUBLE("groove_bevel_double.webp", "Bevel double", "groove_bevel_double.webp", "butt", TypeOfWelds.BW),
    GROOVE_U_SINGLE("groove_u.webp", "U single", "groove_u.webp", "butt", TypeOfWelds.BW),
    GROOVE_U_DOUBLE("groove_u_double.webp", "U double", "groove_u_double.webp", "butt", TypeOfWelds.BW),
    GROOVE_J_SINGLE("groove_j.webp", "J single", "groove_j.webp", "butt", TypeOfWelds.BW),
    GROOVE_J_DOUBLE("groove_j_double.webp", "J double", "groove_j_double.webp", "butt", TypeOfWelds.BW),

    // T-JOINT SIMPLE (FW)
    T_FILLET_SINGLE("t_joint_fillet.webp", "Fillet single", "t_joint_fillet.webp", "t_joint", TypeOfWelds.FW),
    T_FILLET_DOUBLE("t_joint_fillet_double.webp", "Fillet double", "t_joint_fillet_double.webp", "t_joint", TypeOfWelds.FW),
    T_FLARE_BEVEL_SINGLE("t_joint_flare_bevel_groove.webp", "Flare bevel single", "t_joint_flare_bevel_groove.webp", "t_joint", TypeOfWelds.FW),
    T_FLARE_BEVEL_DOUBLE("t_joint_flare_bevel_double_groove.webp", "Flare bevel double", "t_joint_flare_bevel_double_groove.webp", "t_joint", TypeOfWelds.FW),

    // T-JOINT STRESS (BW)
    T_BEVEL_SINGLE("t_joint_bevel.webp", "Bevel single", "t_joint_bevel.webp", "t_joint", TypeOfWelds.BW),
    T_BEVEL_SUPPORT("t_joint_bevel_support.webp", "Bevel with support", "t_joint_bevel_support.webp", "t_joint", TypeOfWelds.BW),
    T_BEVEL_DOUBLE("t_joint_bevel_double.webp", "Bevel double", "t_joint_bevel_double.webp", "t_joint", TypeOfWelds.BW),
    T_J_GROOVE_SINGLE("t_joint_j_groove.webp", "J-groove single", "t_joint_j_groove.webp", "t_joint", TypeOfWelds.BW),
    T_J_GROOVE_DOUBLE("t_joint_j_groove_double.webp", "J-groove double", "t_joint_j_groove_double.webp", "t_joint", TypeOfWelds.BW),

    // LAP SIMPLE (FW)
    LAP_FILLET("lap_joint_fillet.webp", "Fillet lap", "lap_joint_fillet.webp", "lap", TypeOfWelds.FW),
    LAP_SPOT("lap_joint_spot.webp", "Spot lap", "lap_joint_spot.webp", "lap", TypeOfWelds.FW),
    LAP_PLUG("lap_joint_plug_slot.webp", "Plug/Slot lap", "lap_joint_plug_slot.webp", "lap", TypeOfWelds.FW),

    // LAP STRESS (BW)
    LAP_BEVEL("lap_joint_bevel.webp", "Bevel lap", "lap_joint_bevel.webp", "lap", TypeOfWelds.BW),

    // CORNER SIMPLE (FW)
    CORNER_FLUSH("corner_flush.webp", "Flush corner", "corner_flush.webp", "corner", TypeOfWelds.FW),
    CORNER_FLANGE("corner_flange.webp", "Flange corner", "corner_flange.webp", "corner", TypeOfWelds.FW),
    CORNER_EDGE("edge.webp", "Edge corner", "edge.webp", "corner", TypeOfWelds.FW),
    CORNER_FLARE_V("flare_v.webp", "Flare-V corner", "flare_v.webp", "corner", TypeOfWelds.FW),

    // CORNER STRESS (BW)
    CORNER_V_GROOVE("corner_v_groove.webp", "V-groove corner", "corner_v_groove.webp", "corner", TypeOfWelds.BW),
    CORNER_BEVEL_INSIDE("corner_bevel_inside.webp", "Bevel inside corner", "corner_bevel_inside.webp", "corner", TypeOfWelds.BW),
    CORNER_BEVEL_OUTSIDE("corner_bevel_outside.webp", "Bevel outside corner", "corner_bevel_outside.webp", "corner", TypeOfWelds.BW),
    CORNER_J_INSIDE("corner_j_inside.webp", "J-inside corner", "corner_j_inside.webp", "corner", TypeOfWelds.BW),
    CORNER_J_OUTSIDE("corner_j_outside.webp", "J-outside corner", "corner_j_outside.webp", "corner", TypeOfWelds.BW),
    CORNER_U("corner_u.webp", "U corner", "corner_u.webp", "corner", TypeOfWelds.BW),
    CORNER_FULL_OPEN("corner_full_open.webp", "Full open corner", "corner_full_open.webp", "corner", TypeOfWelds.BW),
    CORNER_HALF_OPEN("corner_half_open.webp", "Half open corner", "corner_half_open.webp", "corner", TypeOfWelds.BW);

    fun getAssetPath(): String {
        val folder = when (jointType) {
            "butt" -> "groove"
            "t_joint" -> "t"
            "lap" -> "lap"
            "corner" -> "corner"
            else -> "groove"
        }
        val subFolder = if (typeOfWelds == TypeOfWelds.BW) "stress" else "simple"
        return "edge_preparation/$folder/$subFolder/$assetFileName"
    }

    companion object {
        fun fromId(id: String): EdgePreparation? {
            val found = entries.find { it.id == id }
            if (found != null) return found
            // Migration: old .svg ids -> new .webp
            val migratedId = id.replace(".svg", ".webp")
            return entries.find { it.id == migratedId }
        }
        
        fun getForSelection(
            jointType: String, 
            typeOfWeld: String,
            weldingType: String = "",
            thickness: String = ""
        ): List<EdgePreparation> {
            val typeOfWeldsEnum = TypeOfWelds.fromId(typeOfWeld)
            val thicknessVal = thickness.trim().toDoubleOrNull() ?: 0.0
            val process = WeldingType.fromId(weldingType)?.processName ?: ""

            return entries.filter { prep ->
                if (prep.jointType != jointType || prep.typeOfWelds != typeOfWeldsEnum) {
                    return@filter false
                }

                // Double V Groove — только при толщине >= 6 мм (проверка первой, чтобы гарантировать исключение)
                if (prep == GROOVE_V_DOUBLE && thicknessVal < 6.0) return@filter false

                if (prep == GROOVE_BEVEL_SINGLE) {
                    return@filter when (process) {
                        "GTAW" -> thicknessVal in 1.5..13.0
                        "GMAW", "SAW" -> thicknessVal >= 1.5
                        else -> false
                    }
                }

                // J и U разделка — только при толщине >= 13 мм
                if (thicknessVal < 13.0 && prep in listOf(
                    GROOVE_J_SINGLE, GROOVE_J_DOUBLE, GROOVE_U_SINGLE, GROOVE_U_DOUBLE,
                    T_J_GROOVE_SINGLE, T_J_GROOVE_DOUBLE,
                    CORNER_J_INSIDE, CORNER_J_OUTSIDE, CORNER_U
                )) {
                    return@filter false
                }

                true
            }
        }
    }
}