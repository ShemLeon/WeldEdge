package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.presentation.screen.main.FormField
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@OptIn(ExperimentalMaterial3Api::class)@Composable
fun Thickness(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val types = listOf(
        "1", "1.5", "2", "2.5", "3", "3.5", "4",
        "4.5", "5", "6", "7", "8", "9", "10", "12",
        "15", "18", "20", "25", "30", "40"
    )

    // Состояние фокуса
    var isFocused by remember { mutableStateOf(false) }

    // Значение считается "кастомным", если его нет в списке
    val isCustom = selectedType.isNotEmpty() && !types.contains(selectedType)

    FormField(label = "Толщина наиболее тонкого свариваемого металла (мм)", required = true) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(types) { type ->
                SelectableButton(
                    text = type,
                    isSelected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                )
            }

            item {
                OutlinedTextField(
                    // ПОБЕДА ЗДЕСЬ: Если мы в поле (фокус), показываем всё, что вводим.
                    // Если фокус ушел, показываем только если значения нет в кнопках.
                    value = if (isFocused || isCustom) selectedType else "",
                    onValueChange = { newValue ->
                        val filtered = newValue.replace(',', '.')
                        // Разрешаем ввод цифр, точки и пустой строки
                        if (filtered.isEmpty() || filtered.toDoubleOrNull() != null || filtered == "." || filtered.endsWith(".")) {
                            onTypeSelected(filtered)
                        }
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .onFocusChanged { isFocused = it.isFocused }, // Следим за фокусом
                    placeholder = { Text("Своя...", fontSize = 14.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        // Если ввели своё число и ушли из поля — оставляем синюю рамку
                        unfocusedBorderColor = if (isCustom) Color(0xFF2563EB) else Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = if (isCustom) Color(0xFFEFF6FF) else Color.White,
                        focusedPlaceholderColor = Color(0xFF94A3B8),
                        unfocusedPlaceholderColor = Color(0xFF94A3B8)
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )
            }
        }
    }
}