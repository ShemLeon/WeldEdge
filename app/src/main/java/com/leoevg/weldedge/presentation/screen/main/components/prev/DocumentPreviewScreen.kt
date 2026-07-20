package com.leoevg.weldedge.presentation.screen.main.components.prev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.leoevg.weldedge.R
import com.leoevg.weldedge.presentation.screen.main.MainScreenEvent
import com.leoevg.weldedge.presentation.screen.main.MainScreenState

@Composable
fun DocumentPreviewScreen(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
) {
    val jointTypeLocalized = state.selected.typeOfWeld?.nameEn ?: state.params.jointType
    val typeOfWeldLocalized = when (state.selected.edgePrepSubcategory?.id) {
        "stress" -> stringResource(R.string.type_of_welds_bw)
        "simple" -> stringResource(R.string.type_of_welds_fw)
        else -> state.selected.edgePrepSubcategory?.nameEn ?: state.params.typeOfWeld
    }
    val weldingTypeDisplayName = state.selected.weldingType?.name ?: state.params.weldingType
    val imagePath = state.selected.edgePreparation?.imagePath
        ?.let { "file:///android_asset/$it" }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.preview_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (imagePath != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imagePath,
                            contentDescription = stringResource(R.string.preview_edge_image_description),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                PreviewRow(stringResource(R.string.preview_metal_type), state.params.metalType)
                PreviewRow(stringResource(R.string.preview_metal_type_2), state.params.metalType2)
                PreviewRow(
                    stringResource(R.string.preview_thickness),
                    "${state.params.thickness} ${stringResource(R.string.unit_mm)}"
                )
                PreviewRow(stringResource(R.string.preview_joint_type), jointTypeLocalized)
                PreviewRow(stringResource(R.string.preview_type_of_welds), typeOfWeldLocalized)
                if (state.params.weldingType.isNotEmpty()) {
                    PreviewRow(
                        stringResource(R.string.preview_welding_type),
                        weldingTypeDisplayName
                    )
                }
                if (state.params.engineerName.isNotEmpty()) {
                    PreviewRow(stringResource(R.string.preview_engineer), state.params.engineerName)
                }
                PreviewRow(stringResource(R.string.preview_standard), state.params.standard)
                PreviewRow(
                    stringResource(R.string.preview_wps),
                    if (state.wpsNumber == "_________") "—" else state.wpsNumber
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onEvent(MainScreenEvent.BackClicked) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.preview_back))
            }
            Button(
                onClick = { onEvent(MainScreenEvent.GeneratePdfClicked) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text(stringResource(R.string.preview_download_pdf))
            }
        }
    }
}