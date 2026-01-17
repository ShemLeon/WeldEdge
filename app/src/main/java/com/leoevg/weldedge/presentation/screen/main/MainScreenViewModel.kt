package com.leoevg.weldedge.presentation.screen.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.leoevg.weldedge.generateProfessionalWpsReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    fun onEvent(event: MainScreenEvent, context: Context? = null) {
        when (event) {
            is MainScreenEvent.MetalTypeChanged -> {
                _state.update { it.copy(params = it.params.copy(metalType = event.value)) }
            }
            is MainScreenEvent.ThicknessChanged -> {
                _state.update { 
                    it.copy(
                        params = it.params.copy(thickness = event.value),
                        thicknessError = null
                    ) 
                }
            }
            is MainScreenEvent.JointTypeChanged -> {
                _state.update { it.copy(params = it.params.copy(jointType = event.value)) }
            }
            is MainScreenEvent.ResponsibilityChanged -> {
                _state.update { it.copy(params = it.params.copy(responsibility = event.value)) }
            }
            is MainScreenEvent.EngineerNameChanged -> {
                _state.update { it.copy(params = it.params.copy(engineerName = event.value)) }
            }
            is MainScreenEvent.StandardChanged -> {
                _state.update { it.copy(params = it.params.copy(standard = event.value)) }
            }
            MainScreenEvent.ToggleJointTypeExpanded -> {
                _state.update { it.copy(isJointTypeExpanded = !it.isJointTypeExpanded) }
            }
            MainScreenEvent.SubmitClicked -> {
                val thickness = _state.value.params.thickness.toDoubleOrNull()
                if (thickness == null || thickness <= 0) {
                    _state.update { it.copy(thicknessError = "Укажите корректную толщину металла") }
                } else {
                    _state.update { it.copy(showPreview = true) }
                }
            }
            MainScreenEvent.BackClicked -> {
                _state.update { it.copy(showPreview = false) }
            }
            MainScreenEvent.GeneratePdfClicked -> {
                context?.let {
                    generateProfessionalWpsReport(it, _state.value.params)
                }
            }
        }
    }
}
