package com.leoevg.weldedge.presentation.screen.main

import android.R.attr.value
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.data.local.ResourceManager
import com.leoevg.weldedge.domain.model.EdgePreparation
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.usecase.GenerateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.EdgePreparationChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.EngineerNameChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.JointTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.MetalAlloyConfirmed
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.MetalCategoryChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.MetalTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.MetalType2Changed
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.StandardChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.ThicknessChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.TypeOfWeldChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.WeldingTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.DismissAlloyDialog
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.LanguageChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleJointTypeExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleTypeOfWeldExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleEdgePreparationExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleWeldingTypeExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.SubmitClicked
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.BackClicked
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.GeneratePdfClicked
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val generateReportUseCase: GenerateReportUseCase,
    private val preferencesManager: PreferencesManager,
    private val resourceManager: ResourceManager,
    private val createInitialStateUseCase: CreateInitialStateUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        _state.update { state ->
            val t = state.params.thickness.trim().toDoubleOrNull() ?: 0.0
            val ep = EdgePreparation.fromId(state.params.edgePreparation)
            if (ep == EdgePreparation.GROOVE_V_DOUBLE && t < 6.0) {
                state.copy(params = state.params.copy(edgePreparation = ""))
            } else {
                state
            }
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            DismissAlloyDialog -> onDismissAlloyDialog()
            is LanguageChanged -> onLanguageChanged(event.language)
            ToggleJointTypeExpanded -> onToggleJointTypeExpanded()
            ToggleTypeOfWeldExpanded -> onToggleTypeOfWeldExpanded()
            ToggleEdgePreparationExpanded -> onToggleEdgePreparationExpanded()
            ToggleWeldingTypeExpanded -> onToggleWeldingTypeExpanded()
            SubmitClicked -> onSubmitClicked()
            BackClicked -> onBackClicked()
            GeneratePdfClicked -> onGeneratePdfClicked()
            is OnFieldChanged -> onFieldChangedEvent(event)

        }
    }

    private fun onFieldChangedEvent(event: OnFieldChanged) {
        when (event) {
            is EdgePreparationChanged -> onEdgePreparationChanged(event.value)
            is EngineerNameChanged -> onEngineerNameChanged(event.value)
            is JointTypeChanged -> onJointTypeChanged(event.value)
            is MetalAlloyConfirmed -> onMetalAlloyConfirmed(event.alloy)
            is MetalCategoryChanged -> onMetalCategoryChanged(event.category)
            is MetalTypeChanged -> onMetalTypeChanged(event.value)
            is MetalType2Changed -> onMetalType2Changed(event.value)
            is StandardChanged -> onStandardChanged(event.value)
            is ThicknessChanged -> onThicknessChanged(event.value)
            is TypeOfWeldChanged -> onTypeOfWeldChanged(event.value)
            is WeldingTypeChanged -> onWeldingTypeChanged(event.value)
        }
    }


    private fun createInitialState(): MainScreenState {
        val rawMetal = preferencesManager.getMetalType()
        val metalType = when (rawMetal) {
            null -> "AISI 316L"
            "AISI316L", "316L" -> "AISI 316L"
            "15-5 PH", "AISI 15-5 PH" -> "AISI 630 / 15-5 PH"
            "17-4 PH", "AISI 17-4 PH" -> "AISI 630 / 17-4 PH"
            else -> rawMetal
        }.also {
            if (rawMetal == null || rawMetal in listOf(
                    "AISI316L",
                    "316L",
                    "15-5 PH",
                    "AISI 15-5 PH",
                    "17-4 PH",
                    "AISI 17-4 PH"
                )
            ) preferencesManager.saveMetalType(it)
        }
        val rawMetal2 = preferencesManager.getMetalType2()
        val metalType2 = when (rawMetal2) {
            null -> "AISI 316L"
            "AISI316L", "316L" -> "AISI 316L"
            "15-5 PH", "AISI 15-5 PH" -> "AISI 630 / 15-5 PH"
            "17-4 PH", "AISI 17-4 PH" -> "AISI 630 / 17-4 PH"
            else -> rawMetal2
        }.also {
            if (rawMetal2 == null || rawMetal2 in listOf(
                    "AISI316L",
                    "316L",
                    "15-5 PH",
                    "AISI 15-5 PH",
                    "17-4 PH",
                    "AISI 17-4 PH"
                )
            ) preferencesManager.saveMetalType2(it)
        }
        val jointType = preferencesManager.getJointType() ?: "butt".also {
            preferencesManager.saveJointType(it)
        }
        // Map old stress/simple to BW/FW if necessary, or just use new defaults
        val typeOfWeld = when (val rawResponsibility = preferencesManager.getTypeOfWeld()) {
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
            if (rawWeldingType == null || rawWeldingType == "TIG.svg") preferencesManager.saveWeldingType(
                it
            )
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
            val history =
                preferencesManager.getMetalAlloyHistory(_state.value.selectedMetalCategory)
            _state.update { it.copy(metalAlloyHistory = history) }
        }
    }

    private fun onDismissAlloyDialog() {
        _state.update { it.copy(showAlloyDialog = false) }
    }

    private fun onThicknessChanged(value: String) {
        val thicknessVal = value.toDoubleOrNull() ?: 0.0
        val currentEdgePrep = EdgePreparation.fromId(_state.value.params.edgePreparation)

        val shouldClearEdgePrep = when (currentEdgePrep) {
            EdgePreparation.GROOVE_V_DOUBLE -> thicknessVal < 6.0
            EdgePreparation.GROOVE_J_SINGLE, EdgePreparation.GROOVE_J_DOUBLE,
            EdgePreparation.GROOVE_U_SINGLE, EdgePreparation.GROOVE_U_DOUBLE,
            EdgePreparation.T_J_GROOVE_SINGLE, EdgePreparation.T_J_GROOVE_DOUBLE,
            EdgePreparation.CORNER_J_INSIDE, EdgePreparation.CORNER_J_OUTSIDE,
            EdgePreparation.CORNER_U -> thicknessVal < 13.0

            else -> false
        }

        _state.update {
            it.copy(
                params = it.params.copy(
                    thickness = value,
                    edgePreparation = if (shouldClearEdgePrep) "" else it.params.edgePreparation
                ),
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
        val storedValue = when (value) {
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
        _state.update { state ->
            val t = state.params.thickness.trim().toDoubleOrNull() ?: 0.0
            val ep = EdgePreparation.fromId(value)
            if (ep == EdgePreparation.GROOVE_V_DOUBLE && t < 6.0) return@update state
            state.copy(params = state.params.copy(edgePreparation = value))
        }
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
