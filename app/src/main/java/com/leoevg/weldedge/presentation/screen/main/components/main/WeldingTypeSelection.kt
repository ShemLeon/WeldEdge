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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.leoevg.weldedge.R
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

data class WeldingTypeItem(
    val id: String,
    val label: String,
    val assetPath: String
)

@Composable
fun WeldingTypeSelection(
    selectedType: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onTypeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val items = remember {
        val fullPath = "type_of_welding"
        try {
            context.assets.list(fullPath)?.map { fileName ->
                WeldingTypeItem(
                    id = fileName,
                    label = fileName.removeSuffix(".svg"),
                    assetPath = "file:///android_asset/$fullPath/$fileName"
                )
            }?.sortedBy { it.id } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
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
            label = stringResource(R.string.welding_type_label),
            isRequired = true,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items) { item ->
                    WeldingTypeImage(
                        item = item,
                        isSelected = selectedType == item.id,
                        onClick = { onTypeSelected(item.id) }
                    )
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

@Composable
fun WeldingTypeImage(
    item: WeldingTypeItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
            .then(
                if (isSelected) Modifier.border(2.dp, Color(0xFF3B82F6), RoundedCornerShape(8.dp))
                else Modifier
            )
            .clickable(onClick = onClick)
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
