package com.leoevg.weldedge.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.presentation.screen.main.components.Header

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent // Чтобы градиент из Box был виден
    ) { paddingValues ->
        MainScreenContent(
            state = state,
            onEvent = { event -> viewModel.onEvent(event) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun MainScreenContent(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9))
                )
            )
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                language = state.language,
                onLanguageChange = { onEvent(MainScreenEvent.LanguageChanged(it)) }
            )

            if (!state.showPreview) {
                WeldingForm(
                    params = state.params,
                    isJointTypeExpanded = state.isJointTypeExpanded,
                    thicknessError = state.thicknessError,
                    onEvent = onEvent
                )
            } else {
                DocumentPreview(
                    params = state.params,
                    onBack = { onEvent(MainScreenEvent.BackClicked) },
                    onGeneratePdf = { onEvent(MainScreenEvent.GeneratePdfClicked) }
                )
            }
        }
    }
}

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
            Text(
                text = "Параметры сварочного соединения",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A)
            )

            // Metal Type
            FormField(label = "Вид металла", required = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val types = listOf("нержавейка", "черное железо", "алюминий")
                    types.forEach { type ->
                        SelectableButton(
                            text = type.replaceFirstChar { it.uppercase() },
                            isSelected = params.metalType == type,
                            onClick = { onEvent(MainScreenEvent.MetalTypeChanged(type)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Thickness
            FormField(label = "Толщина наиболее тонкого свариваемого металла (мм)", required = true) {
                OutlinedTextField(
                    value = params.thickness,
                    onValueChange = { onEvent(MainScreenEvent.ThicknessChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Например: 3.0") },
                    isError = thicknessError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )
                if (thicknessError != null) {
                    Text(
                        text = thicknessError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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

            // Engineer Name
            FormField(label = "ФИО инженера (опционально)") {
                OutlinedTextField(
                    value = params.engineerName,
                    onValueChange = { onEvent(MainScreenEvent.EngineerNameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Иванов Иван Иванович") },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )
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

@Composable
fun FormField(label: String, required: Boolean = false, content: @Composable () -> Unit) {
    Column {
        FormFieldLabel(label, required)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun FormFieldLabel(label: String, required: Boolean) {
    Row {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF334155))
        if (required) {
            Text(text = " *", color = Color.Red, fontSize = 14.sp)
        }
    }
}

@Composable
fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0)
        ),
        color = if (isSelected) Color(0xFFEFF6FF) else Color.White
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = textAlign,
                color = if (isSelected) Color(0xFF1E3A8A) else Color(0xFF334155),
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
fun JointTypeCard(
    value: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val resId = when (value) {
                    "стык" -> R.drawable.joint_butt
                    "тавр" -> R.drawable.joint_t
                    "угловой" -> R.drawable.joint_corner
                    "нахлест" -> R.drawable.joint_lap
                    else -> R.drawable.joint_butt
                }
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color(0xFF1E3A8A) else Color(0xFF334155)
            )
        }
    }
}

@Composable
fun DocumentPreview(
    params: WeldingParams,
    onBack: () -> Unit,
    onGeneratePdf: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Превью документа",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Simplified preview info
                PreviewRow("Вид металла", params.metalType)
                PreviewRow("Толщина", "${params.thickness} мм")
                PreviewRow("Тип соединения", params.jointType)
                PreviewRow("Ответственность", params.responsibility)
                if (params.engineerName.isNotEmpty()) {
                    PreviewRow("Инженер", params.engineerName)
                }
                PreviewRow("Стандарт", params.standard)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Назад")
            }
            Button(
                onClick = onGeneratePdf,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text("Скачать PDF")
            }
        }
    }
}

@Composable
fun PreviewRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}
