package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.presentation.screen.main.components.main.FormField

@Composable
fun MetalAlloy(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    preferencesManager: PreferencesManager
) {
    val focusManager = LocalFocusManager.current
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    
    val categories = listOf("Fe", "Fe++", "Al", "Ti")
    val isCategorySelected = selectedType in categories
    
    val detectedCategory = if (isCategorySelected) {
        selectedType
    } else {
        categories.find { selectedType.startsWith(it) }
    }
    
    LaunchedEffect(detectedCategory) {
        if (detectedCategory != null) {
            selectedCategory = detectedCategory
        }
    }
    
    val history = remember(selectedCategory, selectedType) {
        selectedCategory?.let { preferencesManager.getMetalAlloyHistory(it) } ?: emptyList()
    }

    FormField(label = stringResource(R.string.alloy_label), required = true) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AlloyCategorySelector(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onTypeSelected(category)
                }
            )

            if (selectedCategory != null && history.isNotEmpty()) {
                AlloyHistoryChips(
                    history = history,
                    selectedAlloy = if (isCategorySelected) "" else selectedType,
                    onAlloySelected = { alloyName ->
                        focusManager.clearFocus()
                        onTypeSelected(alloyName)
                    }
                )
            }

            AlloyInputField(
                selectedType = selectedType,
                selectedCategory = selectedCategory,
                onTypeSelected = onTypeSelected,
                preferencesManager = preferencesManager
            )
        }
    }
}
