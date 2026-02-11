package com.leoevg.weldedge.presentation.screen.main

import com.leoevg.weldedge.domain.model.WeldingParams

data class MainScreenState(
    val params: WeldingParams = WeldingParams(),
    val showPreview: Boolean = false,
    val isJointTypeExpanded: Boolean = true,
    val isTypeOfWeldExpanded: Boolean = true,
    val isEdgePreparationExpanded: Boolean = true,
    val isWeldingTypeExpanded: Boolean = true,
    val thicknessError: String? = null,
    val language: String = "RU",
    val selectedMetalCategory: String = "Fe",
    val metalAlloyHistory: List<String> = emptyList(),
    val showAlloyDialog: Boolean = false
)
