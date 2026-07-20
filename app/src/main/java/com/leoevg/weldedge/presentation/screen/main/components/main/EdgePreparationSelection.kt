package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.domain.model.EdgePreparationItem
import com.leoevg.weldedge.domain.model.EdgePreparationSubcategory
import com.leoevg.weldedge.presentation.screen.main.SelectableButton
import com.leoevg.weldedge.presentation.utils.baseAssetPath
import com.leoevg.weldedge.presentation.utils.getDrawableResourceById

@Composable
fun EdgePreparationSelection(
    selectedGroup: EdgePreparationGroup?,
    selectedSubcategory: EdgePreparationSubcategory?,
    selectedItem: EdgePreparationItem?,
    onGroupSelected: (EdgePreparationGroup) -> Unit,
    onSubcategorySelected: (EdgePreparationSubcategory) -> Unit,
    onItemSelected: (EdgePreparationItem) -> Unit,
    groups: List<EdgePreparationGroup>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = stringResource(R.string.edge_preparation_label),
            isRequired = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Level 1: joint type groups
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(groups) { group ->
                EdgePrepGroupCard(
                    group = group,
                    isSelected = selectedGroup?.id == group.id,
                    onClick = { onGroupSelected(group) }
                )
            }
        }

        // Level 2: type of weld penetration (shown when a group is selected)
        if (selectedGroup != null) {
            Spacer(modifier = Modifier.height(16.dp))
            DashedDivider()
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(
                label = stringResource(R.string.type_of_welds_label),
                isRequired = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedGroup.subcategories.forEach { sub ->
                    val label = when (sub.id) {
                        "stress" -> stringResource(R.string.type_of_welds_bw)
                        "simple" -> stringResource(R.string.type_of_welds_fw)
                        else -> sub.nameEn
                    }
                    SelectableButton(
                        text = label,
                        isSelected = selectedSubcategory?.id == sub.id,
                        onClick = { onSubcategorySelected(sub) },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Level 3: items (shown when a subcategory is selected)
        if (selectedSubcategory != null) {
            Spacer(modifier = Modifier.height(12.dp))

            if (selectedSubcategory.items.isEmpty()) {
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
                    items(selectedSubcategory.items, key = { it.id }) { item ->
                        EdgePrepItemCard(
                            item = item,
                            isSelected = item.id == selectedItem?.id,
                            onSelected = onItemSelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EdgePrepGroupCard(
    group: EdgePreparationGroup,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = LocalContext.current.getDrawableResourceById(group.iconRes)
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.size(width = 140.dp, height = 120.dp),
        border = CardDefaults.outlinedCardBorder(isSelected).copy(
            width = if (isSelected) 2.dp else 1.dp
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = rememberDrawablePainter(drawable = icon),
                contentDescription = group.nameEn,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isSelected) Color(0xFFDBEAFE) else Color(0xFFF8FAFC)
            ) {
                Text(
                    text = group.nameEn,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color(0xFF1E40AF) else Color(0xFF475569),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EdgePrepItemCard(
    item: EdgePreparationItem,
    isSelected: Boolean,
    onSelected: (EdgePreparationItem) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 210.dp, height = 158.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
            .then(
                if (isSelected) Modifier.border(2.dp, Color(0xFF3B82F6), RoundedCornerShape(8.dp))
                else Modifier
            )
            .clickable { onSelected(item) }
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