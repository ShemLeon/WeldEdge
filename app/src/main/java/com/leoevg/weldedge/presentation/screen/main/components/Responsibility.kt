package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun Responsibility(
    selectedResponsibility: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onResponsibilitySelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    text = "Ответственность шва",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )
                Text(text = " *", color = Color.Red)
            }

            Surface(
                onClick = onToggleExpand,
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Скрыть" else "Показать",
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF475569)
                    )
                }
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val options = listOf(
                    "нагруженный" to "Нагруженный (полная разделка)",
                    "ненагруженный" to "Не требующий полной разделки"
                )
                options.forEach { (value, label) ->
                    SelectableButton(
                        text = label,
                        isSelected = selectedResponsibility == value,
                        onClick = { onResponsibilitySelected(value) },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
