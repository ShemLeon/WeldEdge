package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun MarkMetalSelector(
    data: List<String>,
    onMarkMetalSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedMarkMetal by remember { mutableStateOf(data.first()) }
    if (data.isNotEmpty()) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(data) { alloyName ->
                FilterChip(
                    selected = selectedMarkMetal == alloyName,
                    onClick = { onMarkMetalSelected(alloyName) },
                    label = { Text(alloyName, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEFF6FF),
                        selectedLabelColor = Color(0xFF1E3A8A),
                        containerColor = Color(0xFFF8FAFC),
                        labelColor = Color(0xFF334155)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarkMetalSelectorPreview() {
    MarkMetalSelector(
        data = listOf("St3sp", "09G2S", "AISI 304", "AISI 316L"),
        onMarkMetalSelected = {}
    )
}
