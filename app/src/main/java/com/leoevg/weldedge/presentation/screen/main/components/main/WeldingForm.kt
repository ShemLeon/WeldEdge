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
import androidx.compose.ui.res.stringResource
import com.leoevg.weldedge.R
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.MainScreenState
import com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy.MetalAlloy
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.EdgePreparationChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.JointTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.MetalTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.MetalType2Changed
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.ThicknessChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.TypeOfWeldChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.WeldingTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleJointTypeExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleTypeOfWeldExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleEdgePreparationExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.ToggleWeldingTypeExpanded
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.SubmitClicked

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeldingForm(
    state: MainScreenState.DataSelector,
    onEvent: (MainScreenEvent) -> Unit
) {
    val params = state.params
    val thicknessValue = params.thickness.toDoubleOrNull() ?: 0.0
    val isBWAllowed = thicknessValue >= 1.5

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
            // Welded Metals (two alloy selectors)
            FormField(label = stringResource(R.string.welded_metals_label), required = true) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MetalAlloy(
                        selectedAlloy = params.metalType,
                        onAlloySelected = { onEvent(MetalTypeChanged(it)) }
                    )
                    MetalAlloy(
                        selectedAlloy = params.metalType2,
                        onAlloySelected = { onEvent(MetalType2Changed(it)) }
                    )
                }
            }

            DashedDivider()

            // Thickness
            Thickness(
                selectedType = params.thickness,
                error = state.thicknessError,
                onTypeSelected = { onEvent(ThicknessChanged(it)) },
                data = listOf() // TODO: Add thickness options
            )

            DashedDivider()

            // Joint Type
            JointTypeSelection(
                selectedType = params.jointType,
                isExpanded = state.isJointTypeExpanded,
                onToggleExpand = { onEvent(ToggleJointTypeExpanded) },
                onTypeSelected = { onEvent(JointTypeChanged(it)) },
                data = listOf() // TODO: Add joint types
            )

            DashedDivider()

            // Type of Weld (formerly Responsibility)
            ResponsibilityTypeSelection(
                selectedType = params.typeOfWeld,
                isExpanded = state.isTypeOfWeldExpanded,
                onToggleExpand = { onEvent(ToggleTypeOfWeldExpanded) },
                onTypeSelected = {
                    // Блокируем выбор "BW" на программном уровне, если толщина мала
                    // TODO: вынести логику блокировки в viewModel. если толщина < 3мм - только FW
                    if (it == "BW" && !isBWAllowed) return@ResponsibilityTypeSelection
                    onEvent(TypeOfWeldChanged(it))
                }
            )

            DashedDivider()

            // Edge Preparation
            EdgePreparationSelection(
                jointType = params.jointType,
                typeOfWeld = params.typeOfWeld,
                weldingType = params.weldingType,
                thickness = params.thickness,
                selectedType = params.edgePreparation,
                isExpanded = state.isEdgePreparationExpanded,
                onToggleExpand = { onEvent(ToggleEdgePreparationExpanded) },
                onTypeSelected = { onEvent(EdgePreparationChanged(it)) }
            )

            DashedDivider()

            // Welding Type
            WeldingTypeSelection(
                selectedType = params.weldingType,
                isExpanded = state.isWeldingTypeExpanded,
                onToggleExpand = { onEvent(ToggleWeldingTypeExpanded) },
                onTypeSelected = { onEvent(WeldingTypeChanged(it)) }
            )

            DashedDivider()


            SubmitButton(
                onClick = { onEvent(SubmitClicked) }
            )
        }
    }
}
