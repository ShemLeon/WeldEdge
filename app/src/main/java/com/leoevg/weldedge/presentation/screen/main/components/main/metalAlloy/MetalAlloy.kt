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
import com.leoevg.weldedge.domain.model.Alloy
import com.leoevg.weldedge.domain.model.AlloyCategory
import com.leoevg.weldedge.domain.model.Alloys
import com.leoevg.weldedge.presentation.screen.main.components.main.FormField

@Composable
fun MetalAlloy(
    onAlloySelected: (String) -> Unit,
    dataMetalType: List<String>,
    dataMetalSubType: List<String>,
    label: String? = null // это служебная строка которая не отображается
) {
   val selectedAlloy = remember { mutableStateOf(dataMetalType.first()) }
    if (label != null) {
        FormField(label = label, required = true) {
            Сategory(selectedCategory, alloysInCategory, selectedAlloy, onAlloySelected)
        }
    } else {
        Сategory(selectedCategory, alloysInCategory, selectedAlloy, onAlloySelected)
    }
}

@Composable
private fun Сategory(
    dataMetalSubType: List<String>,
    selectedAlloy: String,
    onAlloySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AlloyCategorySelector(
            selectedCategory = selectedCategory1,
            onCategorySelected = { category ->
                selectedCategory1 = category
                if (alloysInCategory.none { it.name == selectedAlloy }) {
                    // onAlloySelected("")
                }
            }
        )

        if (selectedCategory1 != null) {
            AlloyHistoryChips(
                history = alloysInCategory.map { it.name },
                selectedAlloy = selectedAlloy,
                onAlloySelected = onAlloySelected
            )
        }
    }
    return selectedCategory1
}
