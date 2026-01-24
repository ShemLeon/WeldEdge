package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeldingForm(
    params: WeldingParams,
    isJointTypeExpanded: Boolean,
    isResponsibilityExpanded: Boolean,
    thicknessError: String?,
    onEvent: (MainScreenEvent) -> Unit
) {
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
                selectedType = params.metalType,
                onTypeSelected = { onEvent(MainScreenEvent.MetalTypeChanged(it)) }
            )

            DashedDivider()
            
            // Thickness
            Column {
                Thickness(
                    selectedType = params.thickness,
                    onTypeSelected = { onEvent(MainScreenEvent.ThicknessChanged(it)) }
                )
                if (thicknessError != null) {
                    Text(
                        text = thicknessError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            DashedDivider()

            // Joint Type
            JointTypeSelection(
                selectedType = params.jointType,
                isExpanded = isJointTypeExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleJointTypeExpanded) },
                onTypeSelected = { onEvent(MainScreenEvent.JointTypeChanged(it)) }
            )

            DashedDivider()

            // Responsibility
            Responsibility(
                selectedResponsibility = params.responsibility,
                isExpanded = isResponsibilityExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleResponsibilityExpanded) },
                onResponsibilitySelected = { onEvent(MainScreenEvent.ResponsibilityChanged(it)) }
            )

            DashedDivider()

            // Standard
            StandardSelection(
                selectedStandard = params.standard,
                onStandardSelected = { onEvent(MainScreenEvent.StandardChanged(it)) }
            )

            Button(
                onClick = { onEvent(MainScreenEvent.SubmitClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text(text = "Создать документ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
