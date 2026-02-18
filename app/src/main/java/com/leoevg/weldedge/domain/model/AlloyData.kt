package com.leoevg.weldedge.domain.model

enum class AlloyCategory(
    val displayName: String,
    val colorValue: Long
) {
    ALUMINIUM("AL", 0xFF66BB6A),
    STAINLESS_STEEL("SS", 0xFF42A5F5),
    STEEL("CS", 0xFFFFA726)
}

data class Alloy(
    val name: String, 
    val category: AlloyCategory,
    val awsClassification: String? = null
) {
    fun getFullName(): String {
        if (category == AlloyCategory.STEEL && name.startsWith("S")) return name
        if (name.startsWith("AISI ", ignoreCase = true)) return name
        val prefix = when (category) {
            AlloyCategory.ALUMINIUM -> "AL"
            AlloyCategory.STAINLESS_STEEL -> "SS"
            AlloyCategory.STEEL -> "AISI"
        }
        return "$prefix $name"
    }

    fun getEffectiveAwsClassification(): String {
        if (awsClassification != null) return awsClassification
        return when (category) {
            AlloyCategory.ALUMINIUM -> "A 5.10"
            AlloyCategory.STAINLESS_STEEL -> "A 5.9"
            AlloyCategory.STEEL -> "A 5.18" 
        }
    }
}

object Alloys {
    val allAlloys = listOf(
        // Aluminium (Standard A 5.10)
        Alloy("5052", AlloyCategory.ALUMINIUM),
        Alloy("5086", AlloyCategory.ALUMINIUM),
        Alloy("6061", AlloyCategory.ALUMINIUM),
        Alloy("2014", AlloyCategory.ALUMINIUM),
        Alloy("1100", AlloyCategory.ALUMINIUM),
        
        // Stainless Steel (Standard A 5.9)
        Alloy("AISI 304", AlloyCategory.STAINLESS_STEEL),
        Alloy("AISI 304L", AlloyCategory.STAINLESS_STEEL),
        Alloy("AISI 308L", AlloyCategory.STAINLESS_STEEL),
        Alloy("AISI 316", AlloyCategory.STAINLESS_STEEL),
        Alloy("AISI 316L", AlloyCategory.STAINLESS_STEEL),
        Alloy("AISI 630 / 15-5 PH", AlloyCategory.STAINLESS_STEEL),
        Alloy("AISI 630 / 17-4 PH", AlloyCategory.STAINLESS_STEEL),

        // Steel
        Alloy("AISI 4130", AlloyCategory.STEEL, awsClassification = "A 5.9"),
        Alloy("AISI 4340", AlloyCategory.STEEL, awsClassification = "A 5.28"),
        Alloy("S235JR", AlloyCategory.STEEL, awsClassification = "A 5.18"),
        Alloy("S235J0", AlloyCategory.STEEL, awsClassification = "A 5.18"),
        Alloy("S355JR", AlloyCategory.STEEL, awsClassification = "A 5.18"),
        Alloy("S355J0", AlloyCategory.STEEL, awsClassification = "A 5.18")
    )

    fun getAlloysByCategory(category: AlloyCategory): List<Alloy> {
        return allAlloys.filter { it.category == category }
    }

    fun findByName(name: String): Alloy? {
        val found = allAlloys.find { it.name.equals(name, ignoreCase = true) }
        if (found != null) return found
        // Миграция: "316L" -> "AISI 316L"; "15-5 PH" -> "AISI 630 / 15-5 PH"
        val alt = when (name.trim()) {
            "15-5 PH", "AISI 15-5 PH" -> "AISI 630 / 15-5 PH"
            "17-4 PH", "AISI 17-4 PH" -> "AISI 630 / 17-4 PH"
            else -> if (name.startsWith("AISI ", ignoreCase = true)) name else "AISI $name"
        }
        return allAlloys.find { it.name.equals(alt, ignoreCase = true) }
    }
}
