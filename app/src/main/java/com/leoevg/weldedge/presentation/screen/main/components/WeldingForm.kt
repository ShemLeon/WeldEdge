package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.presentation.screen.main.FormField
import com.leoevg.weldedge.presentation.screen.main.FormFieldLabel
import com.leoevg.weldedge.presentation.screen.main.JointTypeCard
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

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

            // Joint Type
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormFieldLabel(label = "Тип соединения", required = true)
                    TextButton(
                        onClick = { onEvent(MainScreenEvent.ToggleJointTypeExpanded) },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color(0xFFF1F5F9),
                            contentColor = Color(0xFF475569)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(if (isJointTypeExpanded) "Скрыть" else "Показать", fontSize = 14.sp)
                        Icon(
                            imageVector = if (isJointTypeExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = isJointTypeExpanded) {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val joints = listOf(
                            "стык" to "Стыковой",
                            "тавр" to "Тавровый",
                            "угловой" to "Угловой",
                            "нахлест" to "Нахлесточный"
                        )
                        joints.forEach { (value, label) ->
                            JointTypeCard(
                                value = value,
                                label = label,
                                isSelected = params.jointType == value,
                                onClick = { onEvent(MainScreenEvent.JointTypeChanged(value)) }
                            )
                        }
                    }
                }

                if (!isJointTypeExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
                            .border(2.dp, Color(0xFF2563EB), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        val label = when (params.jointType) {
                            "стык" -> "Стыковой"
                            "тавр" -> "Тавровый"
                            "угловой" -> "Угловой"
                            else -> "Нахлесточный"
                        }
                        Text(text = "Выбрано: $label", color = Color(0xFF1E3A8A))
                    }
                }
            }

            // Responsibility
            FormField(label = "Ответственность шва", required = true) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val options = listOf(
                        "нагруженный" to "Нагруженный (полная разделка)",
                        "ненагруженный" to "Не требующий полной разделки"
                    )
                    options.forEach { (value, label) ->
                        SelectableButton(
                            text = label,
                            isSelected = params.responsibility == value,
                            onClick = { onEvent(MainScreenEvent.ResponsibilityChanged(value)) },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            // Standard
            FormField(label = "Стандарт (опционально)") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val standards = listOf("ГОСТ", "AWS", "IAI")
                    standards.forEach { std ->
                        SelectableButton(
                            text = std,
                            isSelected = params.standard == std,
                            onClick = { onEvent(MainScreenEvent.StandardChanged(std)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

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