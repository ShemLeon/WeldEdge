package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun MetalAlloy(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    FormField(label = "Сплав", required = true) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp) // Небольшой отступ для тени/границ первого/последнего элементов
        ) {
            val types = listOf("Fe", "Fe++", "Al", "Ti")
            items(types) { type ->
                SelectableButton(
                    text = type.replaceFirstChar { it.uppercase() },
                    isSelected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                )            }
        }
    }
}
