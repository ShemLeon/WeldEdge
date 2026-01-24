package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

data class EdgePreparationItem(
    val id: String,
    val label: String,
    val iconRes: Int
)

@Composable
fun EdgePreparationSelection(
    selectedType: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onTypeSelected: (String) -> Unit
) {
    val items = listOf(
        EdgePreparationItem(
            "single_v", 
            "Single-V", 
            R.drawable.razdelka_single_v_butt_weld_with_root_face_and_root_gap
        ),
        // Здесь можно добавить другие ассеты, если они есть
        EdgePreparationItem(
            "single_v_2", 
            "Single-V (v2)", 
            R.drawable.razdelka_single_v_butt_weld_with_root_face_and_root_gap
        ),
        EdgePreparationItem(
            "single_v_3", 
            "Single-V (v3)", 
            R.drawable.razdelka_single_v_butt_weld_with_root_face_and_root_gap
        )
    )

    val selectedLabel = items.find { it.id == selectedType }?.label ?: if (selectedType.isEmpty()) "Не выбрано" else selectedType

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = "Подготовка кромок",
            isRequired = false,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(items) { item ->
                    EdgePreparationCard(
                        item = item,
                        isSelected = selectedType == item.id,
                        onClick = { onTypeSelected(item.id) }
                    )
                }
            }
        } else {
            SelectableButton(
                text = "Выбрано: $selectedLabel",
                isSelected = selectedType.isNotEmpty(),
                onClick = onToggleExpand,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdgePreparationCard(
    item: EdgePreparationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.size(width = 160.dp, height = 140.dp),
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
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.label,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isSelected) Color(0xFFDBEAFE) else Color(0xFFF8FAFC)
            ) {
                Text(
                    text = item.label,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color(0xFF1E40AF) else Color(0xFF475569),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}
