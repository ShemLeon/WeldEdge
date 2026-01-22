package com.leoevg.weldedge.presentation.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.presentation.screen.main.components.DocumentPreviewScreen
import com.leoevg.weldedge.presentation.screen.main.components.Header
import com.leoevg.weldedge.presentation.screen.main.components.WeldingForm

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent // Чтобы градиент из Box был виден
    ) { paddingValues ->
        MainScreenContent(
            state = state,
            onEvent = { event -> viewModel.onEvent(event) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun MainScreenContent(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9))
                )
            )
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                language = state.language,
                onLanguageChange = { onEvent(MainScreenEvent.LanguageChanged(it)) }
            )

            if (!state.showPreview) {
                WeldingForm(
                    params = state.params,
                    isJointTypeExpanded = state.isJointTypeExpanded,
                    thicknessError = state.thicknessError,
                    onEvent = onEvent
                )
            } else {
                DocumentPreviewScreen(
                    params = state.params,
                    onBack = { onEvent(MainScreenEvent.BackClicked) },
                    onGeneratePdf = { onEvent(MainScreenEvent.GeneratePdfClicked) }
                )
            }
        }
    }
}



@Composable
fun FormField(label: String, required: Boolean = false, content: @Composable () -> Unit) {
    Column {
        FormFieldLabel(label, required)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun FormFieldLabel(label: String, required: Boolean) {
    Row {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF334155))
        if (required) {
            Text(text = " *", color = Color.Red, fontSize = 14.sp)
        }
    }
}

@Composable
fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0)
        ),
        color = if (isSelected) Color(0xFFEFF6FF) else Color.White
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = textAlign,
                color = if (isSelected) Color(0xFF1E3A8A) else Color(0xFF334155),
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}





@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        state = MainScreenState(
            params = WeldingParams(
                metalType = "Fe",
                thickness = "3.0",
                jointType = "стык"
            )
        ),
        onEvent = {}
    )
}

@Composable
fun PreviewRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}
