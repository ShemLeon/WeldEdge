package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    onTypeSelected: (String) -> Unit
) {
    val jointTypes = JointType.entries.map { jointType ->
        JointTypeItem(jointType.id, stringResource(jointType.nameRes), jointType.iconRes)
    }

    val selectedJointType = JointType.fromId(selectedType)
    val selectedLabel = selectedJointType?.let { stringResource(it.nameRes) } ?: selectedType

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
                items(jointTypes) { item ->
                    JointTypeCard(
                        item = item,
                        isSelected = selectedType == item.id,
                        onClick = { onTypeSelected(item.id) }
                    )
                }
            }
        } else {
            SelectableButton(
                text = stringResource(R.string.selected_format, selectedLabel),
                isSelected = true,
                onClick = onToggleExpand,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

data class JointTypeItem(
    val id: String,
    val label: String,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JointTypeCard(
    item: JointTypeItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
                contentDescription = item.label,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isSelected) Color(0xFFDBEAFE) else Color(0xFFF8FAFC)
            ) {
                Text(
                    text = item.label,
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
