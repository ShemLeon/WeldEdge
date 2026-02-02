package com.leoevg.weldedge.presentation.screen.main.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leoevg.weldedge.R
import com.leoevg.weldedge.domain.model.Responsibility as ResponsibilityEnum
import com.leoevg.weldedge.presentation.screen.main.SelectableButton
import androidx.annotation.StringRes


@Composable
fun Responsibility(
    selectedResponsibility: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onResponsibilitySelected: (String) -> Unit,
    language: String = "RU"
) {
    val options = ResponsibilityEnum.entries.map { it.id to it.getLocalizedName(language) }
    val selectedLabel = ResponsibilityEnum.fromId(selectedResponsibility)?.getLocalizedName(language) ?: selectedResponsibility

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            label = stringResource(R.string.responsibility_label),
            isRequired = true,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { (value, label) ->
                    SelectableButton(
                        text = label,
                        isSelected = selectedResponsibility == value,
                        onClick = { onResponsibilitySelected(value) },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            SelectableButton(
                text = stringResource(R.string.selected_format, selectedLabel),
                isSelected = true,
                onClick = onToggleExpand,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}
