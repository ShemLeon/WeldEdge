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
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.domain.model.Responsibility
import com.leoevg.weldedge.domain.model.WeldingParams
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.leoevg.weldedge.R

@Composable
fun DocumentPreviewScreen(
    params: WeldingParams,
    language: String = "RU",
    onBack: () -> Unit,
    onGeneratePdf: () -> Unit
) {
    val jointTypeLocalized = JointType.fromId(params.jointType)?.getLocalizedName(language) ?: params.jointType
    val responsibilityLocalized = Responsibility.fromId(params.responsibility)?.getLocalizedName(language) ?: params.responsibility
    
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

                // Edge preparation image
                val imagePath = getEdgePreparationImagePath(
                    jointType = params.jointType,
                    responsibility = params.responsibility,
                    edgePreparation = params.edgePreparation
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

                PreviewRow(stringResource(R.string.preview_metal_type), params.metalType)
                PreviewRow(stringResource(R.string.preview_thickness), "${params.thickness} ${stringResource(R.string.unit_mm)}")
                PreviewRow(stringResource(R.string.preview_joint_type), jointTypeLocalized)
                PreviewRow(stringResource(R.string.preview_responsibility), responsibilityLocalized)
                if (params.engineerName.isNotEmpty()) {
                    PreviewRow(stringResource(R.string.preview_engineer), params.engineerName)
                }
                PreviewRow(stringResource(R.string.preview_standard), params.standard)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.preview_back))
            }
            Button(
                onClick = onGeneratePdf,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text(stringResource(R.string.preview_download_pdf))
            }
        }
    }
}