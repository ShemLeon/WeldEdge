package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.presentation.screen.main.components.FormField
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun StandardSelection(
    selectedStandard: String?,
    onStandardSelected: (String) -> Unit
) {
    FormField(label = "Стандарт (опционально)") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val standards = listOf("ГОСТ", "AWS", "IAI")
            standards.forEach { std ->
                SelectableButton(
                    text = std,
                    isSelected = selectedStandard == std,
                    onClick = { onStandardSelected(std) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StandardSelectionPreview() {
    Surface(color = Color(0xFFF8FAFC), modifier = Modifier.padding(16.dp)) {
        StandardSelection(
            selectedStandard = "ГОСТ",
            onStandardSelected = {}
        )
    }
}