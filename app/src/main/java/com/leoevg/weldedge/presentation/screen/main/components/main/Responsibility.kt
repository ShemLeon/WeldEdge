package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun Responsibility(
    selectedResponsibility: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onResponsibilitySelected: (String) -> Unit
) {
    val options = listOf(
        "нагруженный" to "С разделкой",
        "ненагруженный" to "Без разделки"
    )
    val selectedLabel = options.find { it.first == selectedResponsibility }?.second ?: ""

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = "Ответственность шва",
            isRequired = true,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { (value, label) ->
                    SelectableButton(
                        text = label,
                        isSelected = selectedResponsibility == value,
                        onClick = { onResponsibilitySelected(value) },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            SelectableButton(
                text = "Выбрано: $selectedLabel",
                isSelected = true,
                onClick = onToggleExpand,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}
