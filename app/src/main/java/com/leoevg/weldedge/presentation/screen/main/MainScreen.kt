package com.leoevg.weldedge.presentation.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.leoevg.weldedge.presentation.screen.main.components.prev.DocumentPreviewScreen
import com.leoevg.weldedge.presentation.screen.main.components.main.Header
import com.leoevg.weldedge.presentation.screen.main.components.main.WeldingForm
import android.content.res.Configuration
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val localizedContext = remember(state.language) {
        val locale = when (state.language) {
            "RU" -> Locale("ru", "RU")
            "EN" -> Locale("en", "US")
            else -> Locale.getDefault()
        }
        val config = context.resources.configuration // todo - разобратся
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent // Чтобы градиент из Box был виден
    ) { paddingValues ->
        MainScreenContent(
            state = state,
            onEvent = { event -> viewModel.onEvent(event) },
            modifier = Modifier.padding(paddingValues),
            localizedContext = localizedContext
        )
    }
}

@Composable
fun MainScreenContent(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    localizedContext: android.content.Context,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalConfiguration provides Configuration(localizedContext.resources.configuration),
        LocalContext provides localizedContext
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


            when (state) {
                is MainScreenState.DataPreview -> DocumentPreviewScreen(state, onEvent)
                is MainScreenState.DataSelector -> {
                    Header(
                        language = state.language,
                        onLanguageChange = { onEvent(MainScreenEvent.LanguageChanged(it)) }
                    )
                    WeldingForm(state, onEvent)
                }
            }

        }
    }
}}


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
    val context = LocalContext.current
    val previewContext = remember {
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale("ru", "RU"))
        context.createConfigurationContext(config)
    }
    MainScreenContent(
        state = MainScreenState(
            params = WeldingParams(
                metalType = "CS",
                thickness = "3.0",
                jointType = "стык"
            )
        ),
        onEvent = {},
        localizedContext = previewContext
    )
}
