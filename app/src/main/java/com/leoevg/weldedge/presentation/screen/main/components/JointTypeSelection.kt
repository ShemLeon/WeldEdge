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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.presentation.screen.main.FormFieldLabel

@Composable
fun JointTypeSelection(
    selectedType: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onTypeSelected: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormFieldLabel(label = "Тип соединения", required = true)
            TextButton(
                onClick = onToggleExpand,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFFF1F5F9),
                    contentColor = Color(0xFF475569)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(if (isExpanded) "Скрыть" else "Показать", fontSize = 14.sp)
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = isExpanded) {
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
                        isSelected = selectedType == value,
                        onClick = { onTypeSelected(value) }
                    )
                }
            }
        }

        if (!isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
                    .border(2.dp, Color(0xFF2563EB), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                val label = when (selectedType) {
                    "стык" -> "Стыковой"
                    "тавр" -> "Тавровый"
                    "угловой" -> "Угловой"
                    else -> "Нахлесточный"
                }
                Text(text = "Выбрано: $label", color = Color(0xFF1E3A8A))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JointTypeSelectionPreview() {
    Surface(modifier = Modifier.padding(16.dp), color = Color.White) {
        JointTypeSelection(
            selectedType = "тавр",
            isExpanded = true, onToggleExpand = {},
            onTypeSelected = {}
        )
    }
}