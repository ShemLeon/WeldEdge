package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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