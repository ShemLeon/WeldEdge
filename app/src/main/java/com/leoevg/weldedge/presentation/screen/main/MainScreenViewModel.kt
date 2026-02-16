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
    private val _state = MutableStateFlow(createInitialState())

    private fun createInitialState(): MainScreenState {
        val metalType = preferencesManager.getMetalType() ?: "AISI316L".also {
            preferencesManager.saveMetalType(it)
        }
        val metalType2 = preferencesManager.getMetalType2() ?: "AISI316L".also {
            preferencesManager.saveMetalType2(it)
        }
        val jointType = preferencesManager.getJointType() ?: "butt".also {
            preferencesManager.saveJointType(it)
        }
        // Map old stress/simple to BW/FW if necessary, or just use new defaults
        val rawResponsibility = preferencesManager.getTypeOfWeld()
        val typeOfWeld = when (rawResponsibility) {
            "stress" -> "BW"
            "simple" -> "FW"
            else -> rawResponsibility ?: "BW"
        }
        
        val standard = preferencesManager.getStandard() ?: "AWS".also {
            preferencesManager.saveStandard(it)
        }
        val rawWeldingType = preferencesManager.getWeldingType()
        val weldingType = when (rawWeldingType) {
            null, "TIG.svg" -> "type_1_TIG.svg"
            else -> rawWeldingType
        }.also {
            if (rawWeldingType == null || rawWeldingType == "TIG.svg") preferencesManager.saveWeldingType(it)
        }
        
        return MainScreenState(
            params = WeldingParams(
                metalType = metalType,
                metalType2 = metalType2,
                thickness = "2",
                jointType = jointType,
                typeOfWeld = typeOfWeld,
                standard = standard,
                engineerName = preferencesManager.getEngineerName(),
                weldingType = weldingType
            ),
            isJointTypeExpanded = preferencesManager.isJointTypeExpanded(),
            isTypeOfWeldExpanded = preferencesManager.isTypeOfWeldExpanded(),
            isEdgePreparationExpanded = true,
            isWeldingTypeExpanded = preferencesManager.isWeldingTypeExpanded(),
            language = preferencesManager.getLanguage(),
            selectedMetalCategory = "CS",
            metalAlloyHistory = preferencesManager.getMetalAlloyHistory("CS")
        )
    }
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.MetalTypeChanged -> onMetalTypeChanged(event.value)
            is MainScreenEvent.MetalType2Changed -> onMetalType2Changed(event.value)
            is MainScreenEvent.MetalCategoryChanged -> onMetalCategoryChanged(event.category)
            is MainScreenEvent.MetalAlloyConfirmed -> onMetalAlloyConfirmed(event.alloy)
            MainScreenEvent.DismissAlloyDialog -> onDismissAlloyDialog()
            is MainScreenEvent.ThicknessChanged -> onThicknessChanged(event.value)
            is MainScreenEvent.JointTypeChanged -> onJointTypeChanged(event.value)
            is MainScreenEvent.TypeOfWeldChanged -> onTypeOfWeldChanged(event.value)
            is MainScreenEvent.EdgePreparationChanged -> onEdgePreparationChanged(event.value)
            is MainScreenEvent.WeldingTypeChanged -> onWeldingTypeChanged(event.value)
            is MainScreenEvent.EngineerNameChanged -> onEngineerNameChanged(event.value)
            is MainScreenEvent.StandardChanged -> onStandardChanged(event.value)
            is MainScreenEvent.LanguageChanged -> onLanguageChanged(event.language)
            MainScreenEvent.ToggleJointTypeExpanded -> onToggleJointTypeExpanded()
            MainScreenEvent.ToggleTypeOfWeldExpanded -> onToggleTypeOfWeldExpanded()
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

    private fun onMetalType2Changed(value: String) {
        preferencesManager.saveMetalType2(value)
        _state.update { it.copy(params = it.params.copy(metalType2 = value)) }
    }

    private fun onMetalCategoryChanged(category: String) {
        val history = preferencesManager.getMetalAlloyHistory(category)
        _state.update { 
            it.copy(
                selectedMetalCategory = category,
                metalAlloyHistory = history
            ) 
        }
    }

    private fun onMetalAlloyConfirmed(alloy: String) {
        if (alloy.isNotBlank()) {
            preferencesManager.saveMetalAlloyHistory(_state.value.selectedMetalCategory, alloy)
            onMetalTypeChanged(alloy)
            val history = preferencesManager.getMetalAlloyHistory(_state.value.selectedMetalCategory)
            _state.update { it.copy(metalAlloyHistory = history) }
        }
    }

    private fun onDismissAlloyDialog() {
        _state.update { it.copy(showAlloyDialog = false) }
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
        _state.update { 
            it.copy(
                params = it.params.copy(
                    jointType = value,
                    edgePreparation = "" 
                )
            )
        }
    }

    private fun onTypeOfWeldChanged(value: String) {
        // Map BW/FW back to stress/simple for storage if needed, or update PreferencesManager
        val storedValue = when(value) {
            "BW" -> "stress"
            "FW" -> "simple"
            else -> value
        }
        preferencesManager.saveTypeOfWeld(storedValue)
        _state.update { 
            it.copy(
                params = it.params.copy(
                    typeOfWeld = value,
                    edgePreparation = "" 
                )
            )
        }
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

    private fun onToggleTypeOfWeldExpanded() {
        val newState = !_state.value.isTypeOfWeldExpanded
        preferencesManager.saveTypeOfWeldExpanded(newState)
        _state.update { it.copy(isTypeOfWeldExpanded = newState) }
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
            }
        }
    }
}
