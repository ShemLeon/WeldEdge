package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.domain.model.MetalGroup
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.OnMetalGroupChanged

@Composable
fun MetalAlloy(
    dataMetalType: List<MetalGroup>,
    dataMetalSubType: List<String>,
    order: Int,
    onEvent: (MainScreenEvent) -> Unit,

    ) {
    val selectedMetalType by remember { mutableStateOf(dataMetalType.first()) }
    val selectedMarkMetal by remember { mutableStateOf(dataMetalSubType.first()) }
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AlloyCategorySelector(
            data = dataMetalType, onCategorySelected = {
                onEvent(OnMetalGroupChanged(it.name, selectedMarkMetal, order))
            })
        MarkMetalSelector(
            data = dataMetalSubType, onMarkMetalSelected = {
                onEvent(OnMetalGroupChanged(selectedMetalType.name, it, order))
            })
    }
}
