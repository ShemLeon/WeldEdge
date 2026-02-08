package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.MainScreenState
import com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy.MetalAlloy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeldingForm(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit
) {
    val params = state.params
    val thicknessValue = params.thickness.toDoubleOrNull() ?: 0.0
    val isStressAllowed = thicknessValue >= 1.5
    
    Card(
        modifier = Modifier.fillMaxWidth(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Metal Type
            MetalAlloy(
                selectedAlloy = params.metalType,
                onAlloySelected = { onEvent(MainScreenEvent.MetalTypeChanged(it)) }
            )

            DashedDivider()

            // Thickness
            Thickness(
                selectedType = params.thickness,
                error = state.thicknessError,
                onTypeSelected = { onEvent(MainScreenEvent.ThicknessChanged(it)) }
            )

            DashedDivider()

            // Joint Type
            JointTypeSelection(
                selectedType = params.jointType,
                isExpanded = state.isJointTypeExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleJointTypeExpanded) },
                onTypeSelected = { onEvent(MainScreenEvent.JointTypeChanged(it)) }
            )

            DashedDivider()

            // Responsibility
            Responsibility(
                selectedResponsibility = params.responsibility,
                isExpanded = state.isResponsibilityExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleResponsibilityExpanded) },
                onResponsibilitySelected = { 
                    // Блокируем выбор "С разделкой" на программном уровне, если толщина мала
                    if (it == "stress" && !isStressAllowed) return@Responsibility
                    onEvent(MainScreenEvent.ResponsibilityChanged(it)) 
                }
            )

            DashedDivider()

            // Edge Preparation (Виден всегда, но список внутри отфильтрован)
            EdgePreparationSelection(
                jointType = params.jointType,
                responsibility = params.responsibility,
                weldingType = params.weldingType,
                thickness = params.thickness,
                selectedType = params.edgePreparation,
                isExpanded = state.isEdgePreparationExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleEdgePreparationExpanded) },
                onTypeSelected = { onEvent(MainScreenEvent.EdgePreparationChanged(it)) }
            )

            DashedDivider()

            // Welding Type
            WeldingTypeSelection(
                selectedType = params.weldingType,
                isExpanded = state.isWeldingTypeExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleWeldingTypeExpanded) },
                onTypeSelected = { onEvent(MainScreenEvent.WeldingTypeChanged(it)) }
            )

            DashedDivider()


            SubmitButton(
                onClick = { onEvent(MainScreenEvent.SubmitClicked) }
            )
        }
    }
}
