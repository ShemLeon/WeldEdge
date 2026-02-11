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
import com.leoevg.weldedge.domain.model.Alloys
import com.leoevg.weldedge.presentation.screen.main.components.main.FormField

@Composable
fun MetalAlloy(
    selectedAlloy: String,
    onAlloySelected: (String) -> Unit,
    label: String? = null
) {
    var selectedCategory by remember { 
        mutableStateOf(
            Alloys.allAlloys.find { it.name == selectedAlloy }?.category
        ) 
    }

    val alloysInCategory = remember(selectedCategory) {
        selectedCategory?.let { Alloys.getAlloysByCategory(it) } ?: emptyList()
    }

    val content: @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AlloyCategorySelector(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    if (alloysInCategory.none { it.name == selectedAlloy }) {
                        // onAlloySelected("")
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

    if (label != null) {
        FormField(label = label, required = true) {
            content()
        }
    } else {
        content()
    }
}
