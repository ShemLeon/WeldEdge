package com.leoevg.weldedge.domain.model

/**
 * Represents a single WPS specification entry.
 * @param thicknessMin minimum thickness in mm (inclusive)
 * @param thicknessMax maximum thickness in mm (inclusive)
 * @param metal1 alloy category of first metal ("CS", "SS", "AL")
 * @param metal2 alloy category of second metal ("CS", "SS", "AL")
 * @param process welding process ("GTAW", "GMAW", "FCAW", "LBW")
 * @param typeOfWeld list of supported weld types: "BW", "FW", or both
 * @param jointTypes list of joint types ("butt", "t_joint", "corner", "lap"); empty = any
 * @param wpsNumber the WPS document number
 * @param alloy1 specific alloy name (e.g. "4130"); when set, matches only this alloy
 * @param alloy2 specific alloy name; when both set, entry has priority over category-only match
 * @param alloyGroup substring for group match (e.g. "PH"); when set, both metal names must contain it
 */
data class WpsEntry(
    val thicknessMin: Double,
    val thicknessMax: Double,
    val metal1: String,
    val metal2: String,
    val process: String,
    val typeOfWeld: List<String>,
    val jointTypes: List<String> = emptyList(),
    val wpsNumber: String,
    val alloy1: String? = null,
    val alloy2: String? = null,
    val alloyGroup: String? = null
)

object WpsTable {

    val entries = listOf(
        // ===================== GTAW =====================

        // AL + AL, GTAW
        WpsEntry(2.0, 5.0, "AL", "AL", "GTAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(2.0, 8.0, "AL", "AL", "GTAW", listOf("FW"), listOf("t_joint", "corner"), "130"),
        WpsEntry(2.0, 8.0, "AL", "AL", "GTAW", listOf("BW"), listOf("butt"), "131"),
        WpsEntry(2.0, 8.0, "AL", "AL", "GTAW", listOf("FW"), listOf("butt"), "132"),
        WpsEntry(2.0, 8.0, "AL", "AL", "GTAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(2.0, 10.0, "AL", "AL", "GTAW", listOf("FW"), emptyList(), ""),
        WpsEntry(2.0, 13.0, "AL", "AL", "GTAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(7.0, 40.0, "AL", "AL", "GTAW", listOf("BW", "FW"), emptyList(), ""),

        // SS + SS, GTAW
        WpsEntry(2.0, 12.0, "SS", "SS", "GTAW", listOf("BW"), listOf("butt"), "3092"),
        WpsEntry(2.0, 25.0, "SS", "SS", "GTAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(3.0, 10.0, "SS", "SS", "GTAW", listOf("FW"), emptyList(), ""),
        WpsEntry(7.0, 40.0, "SS", "SS", "GTAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(0.0, 50.0, "SS", "SS", "GTAW", listOf("BW", "FW"), emptyList(), ""),

        // CS + CS, GTAW
        WpsEntry(2.0, 12.0, "CS", "CS", "GTAW", listOf("BW"), emptyList(), "3090"),
        WpsEntry(2.0, 12.0, "CS", "CS", "GTAW", listOf("FW"), emptyList(), "3093"),
        WpsEntry(3.0, 999.0, "CS", "CS", "GTAW", listOf("FW"), emptyList(), ""),

        // SS + CS (dissimilar), GTAW
        WpsEntry(2.0, 12.0, "SS", "CS", "GTAW", listOf("FW"), emptyList(), "3089"),
        WpsEntry(2.0, 12.0, "SS", "CS", "GTAW", listOf("BW"), emptyList(), "3091"),
        WpsEntry(3.0, 10.0, "CS", "SS", "GTAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(3.0, 10.0, "CS", "SS", "GTAW", listOf("FW"), emptyList(), ""),
        WpsEntry(2.0, 999.0, "CS", "SS", "GTAW", listOf("FW"), emptyList(), ""),
        WpsEntry(2.001, 999.0, "SS", "CS", "GTAW", listOf("FW"), emptyList(), "3334"), // >2

        // SS (PH), GTAW — 15-5 PH, 17-4 PH
        WpsEntry(7.0, 40.0, "SS", "SS", "GTAW", listOf("FW"), emptyList(), "3333", alloyGroup = "PH"),

        // Special alloys, GTAW
        WpsEntry(5.0, 5.0, "CS", "CS", "GTAW", listOf("BW"), emptyList(), ""),   // CS + 2.5Cr1Mo
        WpsEntry(10.0, 10.0, "CS", "CS", "GTAW", listOf("BW"), emptyList(), ""), // S355J2 + S355J2

        // ===================== GMAW =====================

        // CS + CS, GMAW
        WpsEntry(3.0, 20.0, "CS", "CS", "GMAW", listOf("BW"), emptyList(), "3065"),
        WpsEntry(3.0, 10.0, "CS", "CS", "GMAW", listOf("BW"), listOf("butt"), "3326"),
        WpsEntry(3.0, 10.0, "CS", "CS", "GMAW", listOf("BW"), emptyList(), "135"),
        WpsEntry(3.0, 10.0, "CS", "CS", "GMAW", listOf("BW", "FW"), emptyList(), "3335"), // Prequalified
        WpsEntry(3.0, 20.0, "CS", "CS", "GMAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(5.0, 20.0, "CS", "CS", "GMAW", listOf("BW"), emptyList(), "151", "4130", "4130"), // CS/4130 приоритет
        WpsEntry(7.0, 40.0, "CS", "CS", "GMAW", listOf("BW", "FW"), emptyList(), ""),
        WpsEntry(16.0, 999.0, "CS", "CS", "GMAW", listOf("BW"), emptyList(), "120"),

        // AL + AL, GMAW
        WpsEntry(0.0, 999.0, "AL", "AL", "GMAW", listOf("BW"), emptyList(), "141"),

        // SS + CS (dissimilar), GMAW
        WpsEntry(0.0, 999.0, "SS", "CS", "GMAW", listOf("FW"), emptyList(), "150"),
        WpsEntry(0.0, 999.0, "SS", "CS", "GMAW", listOf("BW"), emptyList(), "153"),

        // ===================== FCAW =====================
        WpsEntry(2.0, 12.0, "CS", "CS", "FCAW", listOf("BW"), emptyList(), "152"),

        // ===================== LBW =====================
        WpsEntry(4.0, 5.0, "AL", "AL", "LBW", listOf("BW"), emptyList(), ""),
        WpsEntry(5.0, 5.0, "AL", "AL", "LBW", listOf("FW"), emptyList(), "")
    )

    /**
     * Find matching WPS entries based on parameters.
     * Matches on: metal categories (and alloy names when specified), process, type of weld, joint type, thickness.
     * Alloy-specific entries (4130+4130) have priority over category-only matches.
     */
    fun findWps(
        metal1Category: String,
        metal2Category: String,
        metal1Name: String,
        metal2Name: String,
        process: String,
        typeOfWeld: String,
        jointType: String,
        thickness: Double
    ): WpsEntry? {
        val matches = entries.filter { entry ->
            // Check process
            if (entry.process != process) return@filter false

            // Check type of weld
            if (typeOfWeld !in entry.typeOfWeld) return@filter false

            // Check joint type (if entry specifies joint types)
            if (entry.jointTypes.isNotEmpty() && jointType !in entry.jointTypes) return@filter false

            // Check thickness in range
            if (thickness < entry.thicknessMin || thickness > entry.thicknessMax) return@filter false

            // Check metals: category match
            val directCatMatch = entry.metal1.equals(metal1Category, ignoreCase = true) &&
                entry.metal2.equals(metal2Category, ignoreCase = true)
            val reverseCatMatch = entry.metal1.equals(metal2Category, ignoreCase = true) &&
                entry.metal2.equals(metal1Category, ignoreCase = true)
            if (!directCatMatch && !reverseCatMatch) return@filter false

            // If entry requires specific alloys, check alloy names (нормализуем AISI 4130 <-> 4130)
            if (entry.alloy1 != null && entry.alloy2 != null) {
                fun norm(s: String) = s.removePrefix("AISI ").trim()
                val directAlloyMatch = norm(metal1Name).equals(norm(entry.alloy1!!), ignoreCase = true) &&
                    norm(metal2Name).equals(norm(entry.alloy2!!), ignoreCase = true)
                val reverseAlloyMatch = norm(metal1Name).equals(norm(entry.alloy2!!), ignoreCase = true) &&
                    norm(metal2Name).equals(norm(entry.alloy1!!), ignoreCase = true)
                if (!directAlloyMatch && !reverseAlloyMatch) return@filter false
            }

            // If entry requires alloy group (e.g. "PH"), both metals must contain it
            if (entry.alloyGroup != null) {
                if (!metal1Name.contains(entry.alloyGroup, ignoreCase = true) ||
                    !metal2Name.contains(entry.alloyGroup, ignoreCase = true)
                ) return@filter false
            }

            true
        }

        // Prefer: alloy-specific > alloy-group > joint-type-specific > has WPS number > thickness range
        return matches
            .sortedWith(
                compareByDescending<WpsEntry> { it.alloy1 != null && it.alloy2 != null }
                    .thenByDescending<WpsEntry> { it.alloyGroup != null }
                    .thenByDescending { it.jointTypes.isNotEmpty() }
                    .thenByDescending { it.wpsNumber.isNotEmpty() }
                    .thenBy { it.thicknessMax - it.thicknessMin }
            )
            .firstOrNull()
    }
}
