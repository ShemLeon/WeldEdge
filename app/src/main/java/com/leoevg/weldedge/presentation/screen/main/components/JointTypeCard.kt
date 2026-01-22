package com.leoevg.weldedge.presentation.screen.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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

@Composable
fun JointTypeCard(
    value: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val resId = when (value) {
                    "стык" -> R.drawable.joint_butt
                    "тавр" -> R.drawable.joint_t
                    "угловой" -> R.drawable.joint_corner
                    "нахлест" -> R.drawable.joint_lap
                    else -> R.drawable.joint_butt
                }
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color(0xFF1E3A8A) else Color(0xFF334155)
            )
        }
    }
}