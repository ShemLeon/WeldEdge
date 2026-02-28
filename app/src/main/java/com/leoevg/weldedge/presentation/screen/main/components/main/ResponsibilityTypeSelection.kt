package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.TypeOfWelds
import com.leoevg.weldedge.presentation.screen.main.SelectableButton

@Composable
fun ResponsibilityTypeSelection(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val options = TypeOfWelds.entries.map {
        it.id to stringResource(it.nameRes) 
    }

    LaunchedEffect(options) {
        if (selectedType.isEmpty() && options.isNotEmpty()) {
            onTypeSelected(options.first().first)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = stringResource(R.string.type_of_welds_label),
            isRequired = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { (value, label) ->
                SelectableButton(
                    text = label,
                    isSelected = selectedType == value,
                    onClick = { onTypeSelected(value) },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
