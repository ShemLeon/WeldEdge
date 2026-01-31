package com.leoevg.weldedge.presentation.screen.main.components.main.metalAlloy

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.data.local.PreferencesManager

@Composable
fun AlloyInputField(
    selectedType: String,
    selectedCategory: String?,
    onTypeSelected: (String) -> Unit,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    
    // Категории для проверки: если выбрана категория, в поле пусто
    val categories = listOf("Fe", "Fe++", "Al", "Ti")
    val isCategorySelected = selectedType in categories
    
    // Если мы не в фокусе и выбрана категория (а не конкретный сплав) — показываем пустоту
    val customAlloyName = if (isCategorySelected) "" else selectedType
    val displayValue = if (isFocused || !isCategorySelected) customAlloyName else ""

    OutlinedTextField(
        value = displayValue,
        onValueChange = { newValue ->
            isFocused = true
            onTypeSelected(newValue)
        },
        modifier = modifier
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
                // Автоматическое сохранение в историю при завершении ввода
                if (selectedCategory != null && displayValue.isNotBlank()) {
                    preferencesManager.saveMetalAlloyHistory(selectedCategory, displayValue)
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
