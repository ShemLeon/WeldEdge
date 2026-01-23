package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.presentation.screen.main.components.FormField
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun Responsibility(
    selectedResponsibility: String,
    onResponsibilitySelected: (String) -> Unit
) {
    FormField(label = "Ответственность шва", required = true) {
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