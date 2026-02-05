package com.leoevg.weldedge.domain.model

enum class AlloyCategory(val displayName: String) {
    ALUMINIUM("Al"),
    STAINLESS_STEEL("SS"),
    STEEL("Fe")
}

data class Alloy(
    val name: String, 
    val category: AlloyCategory,
    val awsClassification: String? = null
) {
    fun getFullName(): String {
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
            AlloyCategory.STEEL -> "A 5.18" // Default for steel if not specified
        }
    }
}

object Alloys {
    val allAlloys = listOf(
        // Aluminium
        Alloy("5052", AlloyCategory.ALUMINIUM),
        Alloy("5086", AlloyCategory.ALUMINIUM),
        Alloy("6061", AlloyCategory.ALUMINIUM),
        Alloy("2014", AlloyCategory.ALUMINIUM),
        Alloy("1100", AlloyCategory.ALUMINIUM),
        // Stainless Steel
        Alloy("304", AlloyCategory.STAINLESS_STEEL),
        Alloy("304L", AlloyCategory.STAINLESS_STEEL),
        Alloy("308L", AlloyCategory.STAINLESS_STEEL),
        // Steel
        Alloy("4130", AlloyCategory.STEEL, awsClassification = "A 5.9"),
        Alloy("4340", AlloyCategory.STEEL, awsClassification = "A 5.28"),
        Alloy("S235JR", AlloyCategory.STEEL, awsClassification = "A 5.18"),
        Alloy("S235J0", AlloyCategory.STEEL, awsClassification = "A 5.18"),
        Alloy("S355JR", AlloyCategory.STEEL, awsClassification = "A 5.18"),
        Alloy("S355J0", AlloyCategory.STEEL, awsClassification = "A 5.18")
    )

    fun getAlloysByCategory(category: AlloyCategory): List<Alloy> {
        return allAlloys.filter { it.category == category }
    }

    fun findByName(name: String): Alloy? {
        return allAlloys.find { it.name.equals(name, ignoreCase = true) }
    }
}
