package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.presentation.screen.main.components.main.FormField

@Composable
fun MetalAlloy(
    selectedAlloy: String,
    onAlloySelected: (String) -> Unit
) {
    var selectedCategory by remember { 
        mutableStateOf(
            Alloys.allAlloys.find { it.name == selectedAlloy }?.category
        ) 
    }

    val alloysInCategory = remember(selectedCategory) {
        selectedCategory?.let { Alloys.getAlloysByCategory(it) } ?: emptyList()
    }

    FormField(label = stringResource(R.string.alloy_label), required = true) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AlloyCategorySelector(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    // При смене категории можно либо сбрасывать выбор, либо оставлять текущий если он совпадает
                    if (alloysInCategory.none { it.name == selectedAlloy }) {
                        // onAlloySelected("") // Опционально: сбрасывать выбор при смене категории
                    }
                }
            )

            if (selectedCategory != null) {
                AlloyHistoryChips(
                    history = alloysInCategory.map { it.name },
                    selectedAlloy = selectedAlloy,
                    onAlloySelected = onAlloySelected
                )
            }
        }
    }
}
