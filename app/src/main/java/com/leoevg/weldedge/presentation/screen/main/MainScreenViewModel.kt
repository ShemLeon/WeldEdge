package com.leoevg.weldedge.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.data.repository.AlloysDatabaseRepository
import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.domain.model.EdgePreparationItem
import com.leoevg.weldedge.domain.model.EdgePreparationSubcategory
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.model.WeldingTypeItem
import com.leoevg.weldedge.domain.usecase.CreateInitialStateUseCase
import com.leoevg.weldedge.domain.usecase.GenerateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.EdgePreparationChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.EdgePrepSubcategoryChanged
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
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.SubmitClicked
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.BackClicked
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.GeneratePdfClicked
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val generateReportUseCase: GenerateReportUseCase,
    private val preferencesManager: PreferencesManager,
    private val createInitialStateUseCase: CreateInitialStateUseCase,
    private val alloysDatabaseRepository: AlloysDatabaseRepository
) : ViewModel() {
    private val _state = MutableStateFlow(createInitialState().withResolvedSelected())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            createInitialStateUseCase.invoke()
                .onSuccess { dbParams ->
                    val database = alloysDatabaseRepository.getDatabase()
                    _state.update { current ->
                        val rawMetal = current.params.metalType
                        val metalType = if (rawMetal.isBlank()) ""
                        else database.findGradeByName(rawMetal) ?: rawMetal

                        val rawMetal2 = current.params.metalType2
                        val metalType2 = if (rawMetal2.isBlank()) ""
                        else database.findGradeByName(rawMetal2) ?: rawMetal2

                        if (metalType != rawMetal) preferencesManager.saveMetalType(metalType)
                        if (metalType2 != rawMetal2) preferencesManager.saveMetalType2(metalType2)

                        val migratedParams = current.params.copy(
                            metalType = metalType,
                            metalType2 = metalType2
                        )
                        current.withResolvedSelected(
                            params = migratedParams,
                            weldingFormParams = dbParams
                        )
                    }
                }
                .onFailure { TODO() }
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            DismissAlloyDialog -> onDismissAlloyDialog()
            is LanguageChanged -> onLanguageChanged(event.language)
            SubmitClicked -> onSubmitClicked()
            BackClicked -> onBackClicked()
            GeneratePdfClicked -> onGeneratePdfClicked()
            is OnFieldChanged -> onFieldChangedEvent(event)
        }
    }

    private fun onFieldChangedEvent(event: OnFieldChanged) {
        when (event) {
            is EdgePreparationChanged -> onEdgePreparationChanged(event.value)
            is EdgePrepSubcategoryChanged -> onEdgePrepSubcategoryChanged(event.value)
            is EngineerNameChanged -> onEngineerNameChanged(event.value)
            is JointTypeChanged -> onJointTypeChanged(event.value)
            is MetalAlloyConfirmed -> onMetalAlloyConfirmed(event.alloy)
            is MetalCategoryChanged -> onMetalCategoryChanged(event.category)
            is OnFieldChanged.OnMetalGroupChanged -> onMetalGroupChanged(
                event.alloy,
                event.markMetal,
                event.order
            )

            is MetalTypeChanged -> onMetalTypeChanged(event.value)
            is MetalType2Changed -> onMetalType2Changed(event.value)
            is StandardChanged -> onStandardChanged(event.value)
            is ThicknessChanged -> onThicknessChanged(event.value)
            is TypeOfWeldChanged -> onTypeOfWeldChanged(event.value)
            is WeldingTypeChanged -> onWeldingTypeChanged(event.value)
        }
    }

    private fun onThicknessChanged(newValue: String) {
        val selected = state.value.selected.copy(thickness = newValue)
        _state.update { it.copy(selected = selected) }
    }

    private fun onEdgePreparationChanged(value: EdgePreparationItem) {
        val selected = state.value.selected.copy(edgePreparation = value)
        _state.update { it.copy(selected = selected) }
    }

    private fun createInitialState(): MainScreenState {
        val metalType = preferencesManager.getMetalType() ?: ""
        val metalType2 = preferencesManager.getMetalType2() ?: ""
        val jointType = preferencesManager.getJointType() ?: "butt".also {
            preferencesManager.saveJointType(it)
        }

        // Migrate legacy typeOfWeld values to canonical "BW"/"FW"
        val typeOfWeld = when (preferencesManager.getTypeOfWeld()) {
            "stress", "bw_preparation" -> "BW"
            "simple", "fw_preparation" -> "FW"
            else -> preferencesManager.getTypeOfWeld() ?: "BW"
        }.also { preferencesManager.saveTypeOfWeld(it) }

        val standard = preferencesManager.getStandard() ?: "AWS".also {
            preferencesManager.saveStandard(it)
        }

        // Migrate legacy weldingType id format (old: "TIG.svg", new: "type_1_TIG")
        val weldingType = when (val raw = preferencesManager.getWeldingType()) {
            null, "TIG.svg", "type_1_TIG.svg" -> "type_1_TIG"
            else -> raw
        }.also { preferencesManager.saveWeldingType(it) }

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
            language = preferencesManager.getLanguage(),
            selectedMetalCategory = preferencesManager.getMetalCategory(),
            metalAlloyHistory = preferencesManager.getMetalAlloyHistory(preferencesManager.getMetalCategory())
        )
    }

    private fun onMetalTypeChanged(value: String) {
        preferencesManager.saveMetalType(value)
        updateParams { it.copy(metalType = value) }
    }

    private fun onMetalType2Changed(value: String) {
        preferencesManager.saveMetalType2(value)
        updateParams { it.copy(metalType2 = value) }
    }

    private fun onMetalGroupChanged(
        @Suppress("UNUSED_PARAMETER") alloy: String,
        markMetal: String,
        order: Int
    ) {
        if (markMetal.isBlank()) return
        when (order) {
            1 -> {
                preferencesManager.saveMetalType(markMetal)
                updateParams { it.copy(metalType = markMetal) }
            }

            2 -> {
                preferencesManager.saveMetalType2(markMetal)
                updateParams { it.copy(metalType2 = markMetal) }
            }
        }
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

    private fun onJointTypeChanged(@Suppress("UNUSED_PARAMETER") value: JointType) {
        // joint type is now set via onTypeOfWeldChanged (edge_preparation group selection)
    }

    private fun onTypeOfWeldChanged(value: EdgePreparationGroup) {
        preferencesManager.saveJointType(value.id)
        preferencesManager.saveTypeOfWeld("")
        val selected = state.value.selected.copy(
            typeOfWeld = value,
            edgePrepSubcategory = null,
            edgePreparation = null
        )
        _state.update {
            it.copy(
                params = it.params.copy(
                    jointType = value.id,
                    typeOfWeld = "",
                    edgePreparation = ""
                ),
                selected = selected
            )
        }
    }

    private fun onEdgePrepSubcategoryChanged(value: EdgePreparationSubcategory) {
        val bwFw = if (value.id == "stress") "BW" else "FW"
        preferencesManager.saveTypeOfWeld(bwFw)
        val selected = state.value.selected.copy(
            edgePrepSubcategory = value,
            edgePreparation = null
        )
        _state.update {
            it.copy(
                params = it.params.copy(typeOfWeld = bwFw, edgePreparation = ""),
                selected = selected
            )
        }
    }

    private fun onWeldingTypeChanged(value: WeldingTypeItem) {
        preferencesManager.saveWeldingType(value.id)
        val selected = state.value.selected.copy(weldingType = value)
        _state.update {
            it.copy(
                params = it.params.copy(weldingType = value.id),
                selected = selected
            )
        }
    }

    private fun onEngineerNameChanged(value: String) {
        preferencesManager.saveEngineerName(value)
        updateParams { it.copy(engineerName = value) }
    }

    private fun onStandardChanged(value: String) {
        preferencesManager.saveStandard(value)
        updateParams { it.copy(standard = value) }
    }

    private fun onLanguageChanged(language: String) {
        preferencesManager.saveLanguage(language)
        _state.update { it.copy(language = language) }
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
            generateReportUseCase(_state.value.params).onFailure { }
        }
    }

    private fun updateParams(transform: (WeldingParams) -> WeldingParams) {
        _state.update { current ->
            current.withResolvedSelected(params = transform(current.params))
        }
    }

    private fun MainScreenState.withResolvedSelected(
        params: WeldingParams = this.params,
        weldingFormParams: BeAvailableWeldingParams = this.weldingFormParams
    ): MainScreenState {
        val database = alloysDatabaseRepository.getDatabase()
        val edgePrepGroup = weldingFormParams.typeOfWeld.firstOrNull { it.id == params.jointType }
        val edgePrepSubcategory = when (params.typeOfWeld) {
            "BW" -> edgePrepGroup?.subcategories?.find { it.id == "stress" }
            "FW" -> edgePrepGroup?.subcategories?.find { it.id == "simple" }
            else -> null
        }
        return copy(
            params = params,
            weldingFormParams = weldingFormParams,
            wpsNumber = params.getWPSnumber(database),
            selected = MainScreenState.Selected(
                metalAlloy1 = resolveMetalAlloySelection(
                    selectedMarkMetal = params.metalType,
                    weldingFormParams = weldingFormParams
                ),
                metalAlloy2 = resolveMetalAlloySelection(
                    selectedMarkMetal = params.metalType2,
                    weldingFormParams = weldingFormParams
                ),
                thickness = params.thickness,
                typeOfWeld = edgePrepGroup,
                edgePrepSubcategory = edgePrepSubcategory,
                edgePreparation = weldingFormParams.edgePreparation.firstOrNull { it.id == params.edgePreparation },
                weldingType = weldingFormParams.weldingType.firstOrNull { it.id == params.weldingType }
            )
        )
    }

    private fun resolveMetalAlloySelection(
        selectedMarkMetal: String,
        weldingFormParams: BeAvailableWeldingParams
    ): MainScreenState.MetalAlloySelection {
        // Ищем группу, которой принадлежит марка
        val metalType = weldingFormParams.metalType.find { group ->
            group.markMetal.any { it.equals(selectedMarkMetal, ignoreCase = true) }
        } ?: weldingFormParams.metalType.firstOrNull()

        val markMetal = when {
            selectedMarkMetal.isNotBlank() -> selectedMarkMetal
            metalType != null -> metalType.markMetal.firstOrNull().orEmpty()
            else -> null
        }

        return MainScreenState.MetalAlloySelection(
            metalType = metalType,
            markMetal = markMetal
        )
    }
}