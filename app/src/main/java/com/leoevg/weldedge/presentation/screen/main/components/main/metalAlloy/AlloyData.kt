package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

enum class AlloyCategory(val displayName: String) {
    ALUMINIUM("Al"),
    STAINLESS_STEEL("SS"),
    STEEL("Fe")
}

data class Alloy(val name: String, val category: AlloyCategory)

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
        Alloy("4130", AlloyCategory.STEEL),
        Alloy("4340", AlloyCategory.STEEL)
    )

    fun getAlloysByCategory(category: AlloyCategory): List<Alloy> {
        return allAlloys.filter { it.category == category }
    }
}