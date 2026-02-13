package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.domain.model.AlloyCategory

@Composable
fun AlloyCategorySelector(
    selectedCategory: AlloyCategory?,
    onCategorySelected: (AlloyCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(AlloyCategory.entries) { category ->
            val accentColor = Color(category.colorValue)
            val isSelected = selectedCategory == category

            Surface(
                onClick = { onCategorySelected(category) },
                modifier = Modifier.heightIn(min = 48.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.5.dp,
                    color = if (isSelected) accentColor else accentColor.copy(alpha = 0.35f)
                ),
                color = if (isSelected) accentColor.copy(alpha = 0.15f) else Color.White
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.displayName,
                        textAlign = TextAlign.Center,
                        color = if (isSelected) Color(0xFF1B1B1F) else Color(0xFF334155),
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlloyCategorySelectorPreview() {
    AlloyCategorySelector(
        selectedCategory = AlloyCategory.STEEL,
        onCategorySelected = {}
    )
}
