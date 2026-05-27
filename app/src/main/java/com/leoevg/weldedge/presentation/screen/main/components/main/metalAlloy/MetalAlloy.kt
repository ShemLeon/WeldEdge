package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.domain.model.MetalGroup
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.MainScreenState
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent.OnFieldChanged.OnMetalGroupChanged

// C:/Users/eliey/AndroidStudioProjects/WeldEdge/app/src/main/java/com/leoevg/weldedge/presentation/screen/main/components/main/metalAlloy/MetalAlloy.kt

@Composable
fun MetalAlloy(
    dataMetalType: List<MetalGroup>,
    dataMetalSubType: List<String>, // Этот параметр можно будет потом удалить
    selected: MainScreenState.MetalAlloySelection,
    order: Int,
    onEvent: (MainScreenEvent) -> Unit,
) {
    // Фильтруем марки: берем из выбранной группы, либо пустой список
    val filteredMarks = selected.metalType?.markMetal ?: emptyList()

    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AlloyCategorySelector(
            data = dataMetalType,
            selected = selected.metalType,
            onCategorySelected = {
                val selectedMarkMetal = selected.markMetal
                val nextMarkMetal = if (selectedMarkMetal != null && selectedMarkMetal in it.markMetal) {
                    selectedMarkMetal
                } else {
                    it.markMetal.firstOrNull().orEmpty()
                }
                onEvent(OnMetalGroupChanged(it.name, nextMarkMetal, order))
            }
        )
        // Теперь передаем отфильтрованные данные
        MarkMetalSelector(
            data = filteredMarks,
            selected = selected.markMetal,
            onMarkMetalSelected = {
                val selectedMetalType = selected.metalType ?: dataMetalType.firstOrNull()
                onEvent(OnMetalGroupChanged(selectedMetalType?.name.orEmpty(), it, order))
            }
        )
    }
}
