package com.leoevg.weldedge.domain.model

enum class EdgePreparation(
    val id: String,
    val displayName: String,
    val assetFileName: String,
    val jointType: String,
    val responsibility: String
) {
    // GROOVE SIMPLE
    GROOVE_SQUARE_SINGLE("groove_1_square_single.svg", "Square single", "groove_1_square_single.svg", "butt", "simple"),
    GROOVE_SQUARE_DOUBLE("groove_2_square_double.svg", "Square double", "groove_2_square_double.svg", "butt", "simple"),
    GROOVE_FLARE_V_SINGLE("groove_3_flare_v.svg", "Flare-V single", "groove_3_flare_v.svg", "butt", "simple"),
    GROOVE_FLARE_V_DOUBLE("groove_4_flare_v_double.svg", "Flare-V double", "groove_4_flare_v_double.svg", "butt", "simple"),

    // GROOVE STRESS
    GROOVE_V_SINGLE("groove_v_single.svg", "V single", "groove_v_single.svg", "butt", "stress"),
    GROOVE_V_DOUBLE("groove_v_double.svg", "V double", "groove_v_double.svg", "butt", "stress"),
    GROOVE_BEVEL_SINGLE("groove_bevel.svg", "Bevel single", "groove_bevel.svg", "butt", "stress"),
    GROOVE_BEVEL_DOUBLE("groove_bevel_double.svg", "Bevel double", "groove_bevel_double.svg", "butt", "stress"),
    GROOVE_U_SINGLE("groove_u.svg", "U single", "groove_u.svg", "butt", "stress"),
    GROOVE_U_DOUBLE("groove_u_double.svg", "U double", "groove_u_double.svg", "butt", "stress"),
    GROOVE_J_SINGLE("groove_j.svg", "J single", "groove_j.svg", "butt", "stress"),
    GROOVE_J_DOUBLE("groove_j_double.svg", "J double", "groove_j_double.svg", "butt", "stress"),

    // T-JOINT SIMPLE
    T_FILLET_SINGLE("t_joint_fillet.svg", "Fillet single", "t_joint_fillet.svg", "t_joint", "simple"),
    T_FILLET_DOUBLE("t_joint_fillet_double.svg", "Fillet double", "t_joint_fillet_double.svg", "t_joint", "simple"),
    T_FLARE_BEVEL_SINGLE("t_joint_flare_bevel_groove.svg", "Flare bevel single", "t_joint_flare_bevel_groove.svg", "t_joint", "simple"),
    T_FLARE_BEVEL_DOUBLE("t_joint_flare_bevel_double_groove.svg", "Flare bevel double", "t_joint_flare_bevel_double_groove.svg", "t_joint", "simple"),

    // T-JOINT STRESS
    T_BEVEL_SINGLE("t_joint_bevel.svg", "Bevel single", "t_joint_bevel.svg", "t_joint", "stress"),
    T_BEVEL_DOUBLE("t_joint_bevel_double.svg", "Bevel double", "t_joint_bevel_double.svg", "t_joint", "stress"),

    // LAP SIMPLE
    LAP_FILLET("lap_joint_fillet.svg", "Fillet lap", "lap_joint_fillet.svg", "lap", "simple"),
    LAP_SPOT("lap_joint_spot.svg", "Spot lap", "lap_joint_spot.svg", "lap", "simple"),
    LAP_PLUG("lap_joint_plug_slot.svg", "Plug/Slot lap", "lap_joint_plug_slot.svg", "lap", "simple"),

    // LAP STRESS
    LAP_BEVEL("lap_joint_bevel.svg", "Bevel lap", "lap_joint_bevel.svg", "lap", "stress"),

    // CORNER SIMPLE
    CORNER_FLUSH("corner_flush.svg", "Flush corner", "corner_flush.svg", "corner", "simple"),
    CORNER_FLANGE("corner_flange.svg", "Flange corner", "corner_flange.svg", "corner", "simple"),
    CORNER_EDGE("edge.svg", "Edge corner", "edge.svg", "corner", "simple"),
    CORNER_FLARE_V("flare_v.svg", "Flare-V corner", "flare_v.svg", "corner", "simple"),

    // CORNER STRESS
    CORNER_V_GROOVE("corner_v_groove.svg", "V-groove corner", "corner_v_groove.svg", "corner", "stress"),
    CORNER_BEVEL_INSIDE("corner_bevel_inside.svg", "Bevel inside corner", "corner_bevel_inside.svg", "corner", "stress"),
    CORNER_BEVEL_OUTSIDE("corner_bevel_outside.svg", "Bevel outside corner", "corner_bevel_outside.svg", "corner", "stress"),
    CORNER_J_INSIDE("corner_j_inside.svg", "J-inside corner", "corner_j_inside.svg", "corner", "stress"),
    CORNER_J_OUTSIDE("corner_j_outside.svg", "J-outside corner", "corner_j_outside.svg", "corner", "stress"),
    CORNER_U("corner_u.svg", "U corner", "corner_u.svg", "corner", "stress"),
    CORNER_FULL_OPEN("corner_full_open.svg", "Full open corner", "corner_full_open.svg", "corner", "stress"),
    CORNER_HALF_OPEN("corner_half_open.svg", "Half open corner", "corner_half_open.svg", "corner", "stress");

    fun getAssetPath(): String {
        val folder = when (jointType) {
            "butt" -> "groove"
            "t_joint" -> "t"
            "lap" -> "lap"
            "corner" -> "corner"
            else -> "groove"
        }
        return "edge_preparation/$folder/$responsibility/$assetFileName"
    }

    companion object {
        fun fromId(id: String): EdgePreparation? = entries.find { it.id == id }
        
        fun getForSelection(jointType: String, responsibility: String): List<EdgePreparation> {
            return entries.filter { it.jointType == jointType && it.responsibility == responsibility }
        }
    }
}
