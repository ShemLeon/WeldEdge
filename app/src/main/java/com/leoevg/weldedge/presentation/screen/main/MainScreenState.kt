package com.leoevg.weldedge.presentation.screen.main

import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.domain.model.EdgePreparationItem
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.domain.model.MetalGroup
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.model.WeldingTypeItem

data class MainScreenState(
    val showPreview: Boolean = false,
    val thicknessError: String? = null,
    val selectedMetalCategory: String = "CS",
    val metalAlloyHistory: List<String> = emptyList(),
    val showAlloyDialog: Boolean = false,
    val language: String = "EN",
    val params: WeldingParams = WeldingParams(),
    val weldingFormParams: BeAvailableWeldingParams = BeAvailableWeldingParams(),
    val selected: Selected = Selected(),
    val wpsNumber: String = ""
) {
    data class Selected(
        val metalAlloy1: MetalAlloySelection = MetalAlloySelection(),
        val metalAlloy2: MetalAlloySelection = MetalAlloySelection(),
        val thickness: String = "",
        val jointType: JointType? = null,
        val typeOfWeld: EdgePreparationGroup? = null,
        val edgePreparation: EdgePreparationItem? = null,
        val weldingType: WeldingTypeItem? = null
    )

    data class MetalAlloySelection(
        val metalType: MetalGroup? = null,
        val markMetal: String? = null
    )
}