package com.leoevg.weldedge.presentation.screen.main

import com.leoevg.weldedge.domain.model.WeldingParams

sealed class MainScreenState {
    val language: String = "EN"
    val params: WeldingParams = WeldingParams()
    data class DataSelector(
        val showPreview: Boolean = false,
        val thicknessError: String? = null,
        val selectedMetalCategory: String = "CS",
        val metalAlloyHistory: List<String> = emptyList(),
        val showAlloyDialog: Boolean = false
    ) : MainScreenState()

    data class DataPreview(
        val showPreview: Boolean = false,
        val thicknessError: String? = null,
        val selectedMetalCategory: String = "CS",
        val metalAlloyHistory: List<String> = emptyList(),
        val showAlloyDialog: Boolean = false
    ) : MainScreenState()
}

