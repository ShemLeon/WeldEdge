package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.EdgePrepSubcategoryChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.ThicknessChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.TypeOfWeldChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.WeldingTypeChanged
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.SubmitClicked



@Composable
fun WeldingForm(
    state: MainScreenState,
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
                        dataMetalType = params.metalType,
                        dataMetalSubType = params.markMetal,
                        selected = state.selected.metalAlloy1,
                        order = 1,
                        onEvent = onEvent
                    )
                    MetalAlloy(
                        dataMetalType = params.metalType,
                        dataMetalSubType = params.markMetal,
                        selected = state.selected.metalAlloy2,
                        order = 2,
                        onEvent = onEvent
                    )
                }
            }

            DashedDivider()

            // Thickness
            Thickness(
                error = state.thicknessError,
                selected = state.selected.thickness,
                onTypeSelected = { onEvent(ThicknessChanged(it)) },
                data = params.thickness
            )

            DashedDivider()

            // Edge Preparation (3-level: joint type → subcategory → item)
            EdgePreparationSelection(
                selectedGroup = state.selected.typeOfWeld,
                selectedSubcategory = state.selected.edgePrepSubcategory,
                selectedItem = state.selected.edgePreparation,
                onGroupSelected = { onEvent(TypeOfWeldChanged(it)) },
                onSubcategorySelected = { onEvent(EdgePrepSubcategoryChanged(it)) },
                onItemSelected = { onEvent(EdgePreparationChanged(it)) },
                groups = params.typeOfWeld
            )

            DashedDivider()

            // Welding Type
            WeldingTypeSelection(
                selected = state.selected.weldingType,
                data = params.weldingType,
                onTypeSelected = { onEvent(WeldingTypeChanged(it)) }
            )

            DashedDivider()


            SubmitButton(
                onClick = { onEvent(SubmitClicked) }
            )
        }
    }
}
