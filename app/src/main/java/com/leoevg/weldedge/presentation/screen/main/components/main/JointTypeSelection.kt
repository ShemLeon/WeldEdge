package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun JointTypeSelection(
    selectedType: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onTypeSelected: (String) -> Unit,
    data: List<JointType>
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = stringResource(R.string.joint_type_label),
            isRequired = true,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(data) { item ->
                    JointTypeCard(
                        item = item,
                        isSelected = selectedType == item.id,
                        onClick = { onTypeSelected(item.id) }
                    )
                }
            }
        } else {
            SelectableButton(
                text = "TODO",
                isSelected = true,
                onClick = onToggleExpand,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JointTypeCard(
    item: JointType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val name = stringResource(id = item.nameRes)
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.size(width = 140.dp, height = 120.dp),
        border = CardDefaults.outlinedCardBorder(isSelected).copy(
            width = if (isSelected) 2.dp else 1.dp
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isSelected) Color(0xFFDBEAFE) else Color(0xFFF8FAFC)
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color(0xFF1E40AF) else Color(0xFF475569),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
