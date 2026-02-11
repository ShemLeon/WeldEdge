package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.EdgePreparation
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

data class EdgePreparationItem(
    val id: String,
    val label: String,
    val assetPath: String
)

@Composable
fun EdgePreparationSelection(
    jointType: String,
    typeOfWeld: String,
    weldingType: String,
    thickness: String,
    selectedType: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onTypeSelected: (String) -> Unit
) {
    val items = remember(jointType, typeOfWeld, weldingType, thickness) {
        EdgePreparation.getForSelection(jointType, typeOfWeld, weldingType, thickness).map { prep ->
            EdgePreparationItem(
                id = prep.id,
                label = prep.displayName,
                assetPath = "file:///android_asset/${prep.getAssetPath()}"
            )
        }
    }

    // Автоматически выбираем первый элемент, если ничего не выбрано
    LaunchedEffect(items) {
        if (selectedType.isEmpty() && items.isNotEmpty()) {
            onTypeSelected(items.first().id)
        }
    }

    val selectedLabel = items.find { it.id == selectedType }?.label ?: if (selectedType.isEmpty()) stringResource(R.string.not_selected) else selectedType

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = stringResource(R.string.edge_preparation_label),
            isRequired = false,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isExpanded) {
            if (items.isEmpty()) {
                Text(
                    text = stringResource(R.string.edge_preparation_none),
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(items) { item ->
                        Box(
                            modifier = Modifier
                                .size(width = 120.dp, height = 90.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedType == item.id) Color(0xFFEFF6FF) else Color.Transparent)
                                .then(
                                    if (selectedType == item.id) Modifier.border(2.dp, Color(0xFF3B82F6), RoundedCornerShape(8.dp))
                                    else Modifier
                                )
                                .clickable { onTypeSelected(item.id) }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = item.assetPath,
                                contentDescription = item.label,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        } else {
            SelectableButton(
                text = stringResource(R.string.selected_format, selectedLabel),
                isSelected = selectedType.isNotEmpty(),
                onClick = onToggleExpand,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
