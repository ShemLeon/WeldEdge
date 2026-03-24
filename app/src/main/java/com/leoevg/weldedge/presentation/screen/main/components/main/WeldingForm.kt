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
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.SubmitClicked

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeldingForm(
    state: MainScreenState.DataSelector,
    onEvent: (MainScreenEvent) -> Unit
) {
    val params = state.weldingFormParams
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
                        dataMetalType = params.metalType[0].second,
                        dataMetalSubType = params.markMetal[0].second,
                        order = 1,
                        onEvent = onEvent
                    )
                    MetalAlloy(
                        dataMetalType = params.metalType[1].second,
                        dataMetalSubType = params.markMetal[1].second,
                        order = 2,
                        onEvent = onEvent
                    )
                }
            }

            DashedDivider()

            // Thickness
            Thickness(
                error = state.thicknessError,
                onTypeSelected = { onEvent(ThicknessChanged(it)) },
                data = params.thickness
            )

            DashedDivider()

            // Joint Type
            JointTypeSelection(
                onTypeSelected = { onEvent(JointTypeChanged(it)) },
                data = params.jointType
            )

            DashedDivider()

            // Type of Weld (formerly Responsibility)
            ResponsibilityTypeSelection(
                onTypeSelected = {
                    // Блокируем выбор "BW" на программном уровне, если толщина мала
                    // TODO: вынести логику блокировки в viewModel. если толщина < 3мм - только FW
                    if (it == "BW" && !isBWAllowed) return@ResponsibilityTypeSelection
                    onEvent(TypeOfWeldChanged(it))
                },
                data = listOf() // TODO: Add types of weld
            )

            DashedDivider()

            // Edge Preparation
            EdgePreparationSelection(
                onTypeSelected = { onEvent(EdgePreparationChanged(it)) },
                data = listOf(),
            )

            DashedDivider()

            // Welding Type
            WeldingTypeSelection(
                selectedType = params.weldingType,
                onTypeSelected = { onEvent(WeldingTypeChanged(it)) }
            )

            DashedDivider()


            SubmitButton(
                onClick = { onEvent(SubmitClicked) }
            )
        }
    }
}
