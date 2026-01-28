package com.leoevg.weldedge.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.usecase.GenerateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val generateReportUseCase: GenerateReportUseCase,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(
        MainScreenState(
            params = WeldingParams(
                metalType = preferencesManager.getMetalType() ?: "нержавейка",
                jointType = preferencesManager.getJointType() ?: "стык",
                responsibility = preferencesManager.getResponsibility() ?: "нагруженный",
                standard = preferencesManager.getStandard() ?: "ГОСТ",
                engineerName = preferencesManager.getEngineerName(),
                weldingType = preferencesManager.getWeldingType() ?: ""
            ),
            isJointTypeExpanded = preferencesManager.isJointTypeExpanded(),
            isResponsibilityExpanded = preferencesManager.isResponsibilityExpanded(),
            isEdgePreparationExpanded = true,
            isWeldingTypeExpanded = preferencesManager.isWeldingTypeExpanded(),
            language = preferencesManager.getLanguage()
        )
    )
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.MetalTypeChanged -> onMetalTypeChanged(event.value)
            is MainScreenEvent.ThicknessChanged -> onThicknessChanged(event.value)
            is MainScreenEvent.JointTypeChanged -> onJointTypeChanged(event.value)
            is MainScreenEvent.ResponsibilityChanged -> onResponsibilityChanged(event.value)
            is MainScreenEvent.EdgePreparationChanged -> onEdgePreparationChanged(event.value)
            is MainScreenEvent.WeldingTypeChanged -> onWeldingTypeChanged(event.value)
            is MainScreenEvent.EngineerNameChanged -> onEngineerNameChanged(event.value)
            is MainScreenEvent.StandardChanged -> onStandardChanged(event.value)
            is MainScreenEvent.LanguageChanged -> onLanguageChanged(event.language)
            MainScreenEvent.ToggleJointTypeExpanded -> onToggleJointTypeExpanded()
            MainScreenEvent.ToggleResponsibilityExpanded -> onToggleResponsibilityExpanded()
            MainScreenEvent.ToggleEdgePreparationExpanded -> onToggleEdgePreparationExpanded()
            MainScreenEvent.ToggleWeldingTypeExpanded -> onToggleWeldingTypeExpanded()
            MainScreenEvent.SubmitClicked -> onSubmitClicked()
            MainScreenEvent.BackClicked -> onBackClicked()
            MainScreenEvent.GeneratePdfClicked -> onGeneratePdfClicked()
        }
    }

    private fun onMetalTypeChanged(value: String) {
        preferencesManager.saveMetalType(value)
        _state.update { it.copy(params = it.params.copy(metalType = value)) }
    }

    private fun onThicknessChanged(value: String) {
        _state.update {
            it.copy(
                params = it.params.copy(thickness = value),
                thicknessError = null
            )
        }
    }

    private fun onJointTypeChanged(value: String) {
        preferencesManager.saveJointType(value)
        _state.update { it.copy(params = it.params.copy(jointType = value)) }
    }

    private fun onResponsibilityChanged(value: String) {
        preferencesManager.saveResponsibility(value)
        _state.update { it.copy(params = it.params.copy(responsibility = value)) }
    }

    private fun onEdgePreparationChanged(value: String) {
        _state.update { it.copy(params = it.params.copy(edgePreparation = value)) }
    }

    private fun onWeldingTypeChanged(value: String) {
        preferencesManager.saveWeldingType(value)
        _state.update { it.copy(params = it.params.copy(weldingType = value)) }
    }

    private fun onEngineerNameChanged(value: String) {
        preferencesManager.saveEngineerName(value)
        _state.update { it.copy(params = it.params.copy(engineerName = value)) }
    }

    private fun onStandardChanged(value: String) {
        preferencesManager.saveStandard(value)
        _state.update { it.copy(params = it.params.copy(standard = value)) }
    }

    private fun onLanguageChanged(language: String) {
        preferencesManager.saveLanguage(language)
        _state.update { it.copy(language = language) }
    }

    private fun onToggleJointTypeExpanded() {
        val newState = !_state.value.isJointTypeExpanded
        preferencesManager.saveJointTypeExpanded(newState)
        _state.update { it.copy(isJointTypeExpanded = newState) }
    }

    private fun onToggleResponsibilityExpanded() {
        val newState = !_state.value.isResponsibilityExpanded
        preferencesManager.saveResponsibilityExpanded(newState)
        _state.update { it.copy(isResponsibilityExpanded = newState) }
    }

    private fun onToggleEdgePreparationExpanded() {
        _state.update { it.copy(isEdgePreparationExpanded = !it.isEdgePreparationExpanded) }
    }

    private fun onToggleWeldingTypeExpanded() {
        val newState = !_state.value.isWeldingTypeExpanded
        preferencesManager.saveWeldingTypeExpanded(newState)
        _state.update { it.copy(isWeldingTypeExpanded = newState) }
    }

    private fun onSubmitClicked() {
        val thickness = _state.value.params.thickness.toDoubleOrNull()
        if (thickness == null || thickness <= 0) {
            _state.update { it.copy(thicknessError = "Укажите корректную толщину металла") }
        } else {
            _state.update { it.copy(showPreview = true) }
        }
    }

    private fun onBackClicked() {
        _state.update { it.copy(showPreview = false) }
    }

    private fun onGeneratePdfClicked() {
        viewModelScope.launch {
            generateReportUseCase(_state.value.params).onFailure {
                // Here you could handle errors via state, e.g., show a snackbar event
            }
        }
    }
}
