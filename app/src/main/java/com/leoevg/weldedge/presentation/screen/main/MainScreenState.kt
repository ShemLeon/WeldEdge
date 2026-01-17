package com.leoevg.weldedge.presentation.screen.main

import com.leoevg.weldedge.domain.model.WeldingParams

data class MainScreenState(
    val params: WeldingParams = WeldingParams(),
    val showPreview: Boolean = false,
    val isJointTypeExpanded: Boolean = true,
    val thicknessError: String? = null,
    val language: String = "RU"
)
