package com.leoevg.weldedge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.weldedge.presentation.screen.main.MainScreen
import com.leoevg.weldedge.presentation.screen.main.MainScreenViewModel
import com.leoevg.weldedge.ui.theme.WeldEdgeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: MainScreenViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(state.language) {
                val localeTag = when (state.language) {
                    "RU" -> "ru-RU"
                    "EN" -> "en-US"
                    else -> "ru-RU"
                }
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(localeTag)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }

            WeldEdgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
