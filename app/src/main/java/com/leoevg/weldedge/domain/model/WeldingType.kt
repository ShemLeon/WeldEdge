package com.leoevg.weldedge.domain.model

enum class WeldingType(
    val id: String,
    val displayName: String,
    val processName: String,
    val assetName: String
) {
    TIG("type_1_TIG.svg", "TIG", "GTAW", "type_1_TIG.svg"),
    MIG_MAG("type_2_MAG-MIG.svg", "MIG / MAG", "GMAW", "type_2_MAG-MIG.svg"),
    MMA("type_3_MMA.svg", "MMA / Stick", "SMAW", "type_3_MMA.svg"),
    FCAW("type_4_FCAW.svg", "FCAW", "FCAW", "type_4_FCAW.svg");

    companion object {
        fun fromId(id: String): WeldingType? = entries.find { it.id == id }
    }
}
