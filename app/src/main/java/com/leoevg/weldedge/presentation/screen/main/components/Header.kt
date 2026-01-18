package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun Header(
    language: String,
    onLanguageChange: (String) -> Unit
) {
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section with Date and Language
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White,
                shadowElevation = 2.dp,
            ) {
                Text(
                    text = getFormattedDate(language),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF2563EB),
                )
            }

            // Language Selector
            Box {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(onClick = { isLanguageMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = Color(0xFF475569)
                        )
                    }
                }

                DropdownMenu(
                    expanded = isLanguageMenuExpanded,
                    onDismissRequest = { isLanguageMenuExpanded = false }
                ) {
                    listOf("RU", "EN").forEach { lang ->
                        DropdownMenuItem(
                            text = { Text(lang) },
                            onClick = {
                                onLanguageChange(lang)
                                isLanguageMenuExpanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = if (language == lang) Color(0xFF2563EB) else Color.Black
                            )
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun getFormattedDate(language: String): String {
    val calendar = Calendar.getInstance()
    val locale = when (language) {
        "RU" -> Locale("ru", "RU")
        "EN" -> Locale("en", "US")
        else -> Locale.getDefault()
    }
    val dateFormat = SimpleDateFormat("EEEE, d MMMM", locale)
    return dateFormat.format(calendar.time).replaceFirstChar { it.uppercase() }
}
