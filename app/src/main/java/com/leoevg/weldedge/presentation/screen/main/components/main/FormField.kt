package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormField(
    label: String,
    required: Boolean = false,
    content: @Composable () -> Unit
) {
    Column {
        FormFieldLabel(label, required)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun FormFieldLabel(label: String, required: Boolean) {
    Row {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF334155)
        )
        if (required) {
            Text(
                text = " *",
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}

// --- Секция Preview ---

@Preview(showBackground = true, name = "Form Fields Example")
@Composable
private fun FormFieldPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            // Пример обязательного поля
            FormField(label = "Имя пользователя", required = true) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Введите имя") }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Пример необязательного поля
            FormField(label = "Комментарий", required = false) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Необязательно") }
                )
            }
        }
    }
}