package com.leoevg.weldedge.presentation.screen.main


import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.domain.model.EdgePreparationItem
import com.leoevg.weldedge.domain.model.EdgePreparationSubcategory
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.domain.model.WeldingTypeItem

sealed interface MainScreenEvent {
    sealed interface OnFieldChanged : MainScreenEvent {
        data class MetalTypeChanged(val value: String) : OnFieldChanged
        data class MetalType2Changed(val value: String) : OnFieldChanged
        data class MetalCategoryChanged(val category: String) : OnFieldChanged
        data class MetalAlloyConfirmed(val alloy: String) : OnFieldChanged
        data class ThicknessChanged(val value: String) : OnFieldChanged
        data class JointTypeChanged(val value: JointType) : OnFieldChanged
        data class TypeOfWeldChanged(val value: EdgePreparationGroup) : OnFieldChanged
        data class EdgePrepSubcategoryChanged(val value: EdgePreparationSubcategory) : OnFieldChanged
        data class EdgePreparationChanged(val value: EdgePreparationItem) : OnFieldChanged
        data class WeldingTypeChanged(val value: WeldingTypeItem) : OnFieldChanged
        data class EngineerNameChanged(val value: String) : OnFieldChanged
        data class StandardChanged(val value: String) : OnFieldChanged
        // ордер может быть только 1 или 2
        data class OnMetalGroupChanged(val alloy: String, val markMetal: String, val order: Int) : OnFieldChanged
    }

    object DismissAlloyDialog : MainScreenEvent
    data class LanguageChanged(val language: String) : MainScreenEvent
    object SubmitClicked : MainScreenEvent
    object BackClicked : MainScreenEvent
    object GeneratePdfClicked : MainScreenEvent
}
