package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Thickness(
    selectedType: String,
    error: String? = null,
    data: List<String>,
    onTypeSelected: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var lastClickedButton by remember { mutableStateOf("") }

    Column {
        FormField(label = stringResource(R.string.thickness_label), required = true) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(data) { type ->
                    SelectableButton(
                        text = type,
                        isSelected = selectedType == type,
                        onClick = {
                            focusManager.clearFocus() // Убираем курсор из поля при клике на кнопку
                            lastClickedButton = type
                            onTypeSelected(type)
                        }
                    )
                }

                item {
                    OutlinedTextField(
                        value = if (isFocused || selectedType != lastClickedButton) selectedType else "",
                        onValueChange = { newValue ->
                            val filtered = newValue.replace(',', '.')
                            if (filtered.isEmpty() || filtered.toDoubleOrNull() != null || filtered == "." || filtered.endsWith(".")) {
                                lastClickedButton = ""
                                onTypeSelected(filtered)
                            }
                        },
                        modifier = Modifier
                            .width(150.dp)
                            .onFocusChanged { isFocused = it.isFocused },
                        placeholder = { Text(stringResource(R.string.thickness_placeholder), fontSize = 14.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done // Кнопка "Галочка/Готово" на клавиатуре
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus() // Прячем клавиатуру и УБИРАЕМ КУРСОР
                            }
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2563EB),
                            cursorColor = Color(0xFF2563EB), // Цвет палочки
                            unfocusedBorderColor = if (selectedType.isNotEmpty() && selectedType != lastClickedButton) Color(0xFF2563EB) else Color(0xFFE2E8F0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = if (selectedType.isNotEmpty() && selectedType != lastClickedButton) Color(0xFFEFF6FF) else Color.White,
                            focusedPlaceholderColor = Color(0xFF94A3B8),
                            unfocusedPlaceholderColor = Color(0xFF94A3B8)
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                    )
                }
            }
        }
        
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}
