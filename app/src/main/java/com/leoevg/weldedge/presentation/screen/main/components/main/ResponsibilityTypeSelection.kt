package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.presentation.screen.main.SelectableButton
import com.leoevg.weldedge.presentation.utils.getStringResourceById

@Composable
fun ResponsibilityTypeSelection(
    selected: EdgePreparationGroup?,
    onTypeSelected: (EdgePreparationGroup) -> Unit,
    data: List<EdgePreparationGroup>
) {
    val context = LocalContext.current
    val options = data.map {
        it to context.getStringResourceById(it.nameRes)
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
            options.forEach { (item, label) ->
                SelectableButton(
                    text = label,
                    isSelected = selected?.id == item.id,
                    onClick = { onTypeSelected(item) },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
