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
import com.leoevg.weldedge.domain.model.EdgePreparationItem

const val baseAssetPath = "file:///android_asset/"

@Composable
fun EdgePreparationSelection(
    onTypeSelected: (String) -> Unit,
    data: List<EdgePreparationItem>
) {
    val selectedTypeId = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = stringResource(R.string.edge_preparation_label),
            isRequired = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (data.isEmpty()) {
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
                items(data, key = { it.id }) { item ->
                    EachPreparationContent(
                        item = item,
                        onTypeSelected = {
                            selectedTypeId.value = it
                            onTypeSelected.invoke(it)
                        },
                        isSelected = item.id == selectedTypeId.value
                    )
                }
            }
        }
    }
}

@Composable
private fun EachPreparationContent(
    item: EdgePreparationItem,
    onTypeSelected: (String) -> Unit,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(width = 210.dp, height = 158.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    Color(0xFF3B82F6),
                    RoundedCornerShape(8.dp)
                )
                else Modifier
            )
            .clickable { onTypeSelected(item.id) }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = baseAssetPath + item.imagePath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
