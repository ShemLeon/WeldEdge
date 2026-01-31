package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetalAlloy(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    preferencesManager: PreferencesManager
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    
    // Определяем категорию из selectedType
    val categories = listOf("Fe", "Fe++", "Al", "Ti")
    val isCategorySelected = selectedType in categories
    
    // Определяем категорию: если selectedType это категория, используем её, иначе ищем по началу строки
    val detectedCategory = if (isCategorySelected) {
        selectedType
    } else {
        categories.find { selectedType.startsWith(it) }
    }
    
    // Инициализируем selectedCategory
    LaunchedEffect(detectedCategory) {
        if (detectedCategory != null) {
            selectedCategory = detectedCategory
        }
    }
    
    // Получаем историю для выбранной категории
    val history = remember(selectedCategory, selectedType) {
        selectedCategory?.let { preferencesManager.getMetalAlloyHistory(it) } ?: emptyList()
    }
    
    // Определяем название сплава для отображения в поле
    val customAlloyName = if (isCategorySelected) "" else selectedType

    FormField(label = "Сплав", required = true) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Категории сплавов
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(categories) { category ->
                    SelectableButton(
                        text = category.replaceFirstChar { it.uppercase() },
                        isSelected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            onTypeSelected(category)
                        }
                    )
                }
            }
            
            // Чипсы с историей выбранных сплавов
            if (selectedCategory != null && history.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(history) { alloyName ->
                        FilterChip(
                            selected = customAlloyName == alloyName,
                            onClick = {
                                focusManager.clearFocus()
                                onTypeSelected(alloyName)
                            },
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
            
            // Поле для ввода кастомного сплава
            OutlinedTextField(
                value = if (isFocused || !isCategorySelected) customAlloyName else "",
                onValueChange = { newValue ->
                    isFocused = true
                    onTypeSelected(newValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                label = { Text("Или введите название", fontSize = 12.sp) },
                placeholder = { Text("Например: AISI 316L", fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        // Сохраняем в историю при вводе
                        if (selectedCategory != null && customAlloyName.isNotBlank()) {
                            preferencesManager.saveMetalAlloyHistory(selectedCategory!!, customAlloyName)
                        }
                    }
                ),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )
        }
    }
}
