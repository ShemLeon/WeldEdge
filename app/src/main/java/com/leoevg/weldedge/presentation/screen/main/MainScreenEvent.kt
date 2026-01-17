package com.leoevg.weldedge.presentation.screen.main

sealed class MainScreenEvent {
    data class MetalTypeChanged(val value: String) : MainScreenEvent()
    data class ThicknessChanged(val value: String) : MainScreenEvent()
    data class JointTypeChanged(val value: String) : MainScreenEvent()
    data class ResponsibilityChanged(val value: String) : MainScreenEvent()
    data class EngineerNameChanged(val value: String) : MainScreenEvent()
    data class StandardChanged(val value: String) : MainScreenEvent()
    data class LanguageChanged(val language: String) : MainScreenEvent()
    object SubmitClicked : MainScreenEvent()
    object BackClicked : MainScreenEvent()
    object ToggleJointTypeExpanded : MainScreenEvent()
    object GeneratePdfClicked : MainScreenEvent()
}
