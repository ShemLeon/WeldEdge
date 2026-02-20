package com.leoevg.weldedge.presentation.screen.main

sealed interface MainScreenEvent {
    sealed interface OnFieldChanged : MainScreenEvent {
        data class MetalTypeChanged(val value: String) : OnFieldChanged
        data class MetalType2Changed(val value: String) : OnFieldChanged
        data class MetalCategoryChanged(val category: String) : OnFieldChanged
        data class MetalAlloyConfirmed(val alloy: String) : OnFieldChanged
        data class ThicknessChanged(val value: String) : OnFieldChanged
        data class JointTypeChanged(val value: String) : OnFieldChanged
        data class TypeOfWeldChanged(val value: String) : OnFieldChanged
        data class EdgePreparationChanged(val value: String) : OnFieldChanged
        data class WeldingTypeChanged(val value: String) : OnFieldChanged
        data class EngineerNameChanged(val value: String) : OnFieldChanged
        data class StandardChanged(val value: String) : OnFieldChanged
    }

    object DismissAlloyDialog : MainScreenEvent
    data class LanguageChanged(val language: String) : MainScreenEvent
    object SubmitClicked : MainScreenEvent
    object BackClicked : MainScreenEvent
    object ToggleJointTypeExpanded : MainScreenEvent
    object ToggleTypeOfWeldExpanded : MainScreenEvent
    object ToggleEdgePreparationExpanded : MainScreenEvent
    object ToggleWeldingTypeExpanded : MainScreenEvent
    object GeneratePdfClicked : MainScreenEvent
}
