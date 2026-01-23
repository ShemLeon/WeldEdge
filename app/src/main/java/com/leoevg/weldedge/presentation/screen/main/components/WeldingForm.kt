package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeldingForm(
    params: WeldingParams,
    isJointTypeExpanded: Boolean,
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
            // Thickness
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
            JointTypeSelection(
                selectedType = params.jointType,
                isExpanded = isJointTypeExpanded,
                onToggleExpand = { onEvent(MainScreenEvent.ToggleJointTypeExpanded) },
                onTypeSelected = { onEvent(MainScreenEvent.JointTypeChanged(it)) }
            )
            Responsibility(
                selectedResponsibility = params.responsibility,
                onResponsibilitySelected = { onEvent(MainScreenEvent.ResponsibilityChanged(it)) }
            )
            StandardSelection(
                selectedStandard = params.standard,
                onStandardSelected = { onEvent(MainScreenEvent.StandardChanged(it)) }
            )
            SubmitButton(
                onClick = { onEvent(MainScreenEvent.SubmitClicked) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeldingFormPreview() {
    // Создаем тестовые параметры
    val mockParams = WeldingParams(
        metalType = "Сталь",
        thickness = "2.5",
        jointType = "тавр",
        responsibility = "нагруженный",
        engineerName = "Иванов И.И.",
        standard = "ГОСТ"
    )

    Box(
        modifier = Modifier
            .background(Color(0xFFF8FAFC)) // Цвет фона как в приложении
            .padding(16.dp)
    ) {
        WeldingForm(
            params = mockParams,
            isJointTypeExpanded = true,
            thicknessError = null,
            onEvent = {} // Пустая лямбда для превью
        )
    }
}

@Preview(showBackground = true, name = "Welding Form With Error")
@Composable
fun WeldingFormErrorPreview() {
    val mockParams = WeldingParams(
        metalType = "Алюминий",
        thickness = "",
        jointType = "стык",
        responsibility = "ненагруженный"
    )

    Box(
        modifier = Modifier
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        WeldingForm(
            params = mockParams,
            isJointTypeExpanded = false,
            thicknessError = "Поле обязательно для заполнения",
            onEvent = {}
        )
    }
}