package com.leoevg.weldedge.presentation.screen.main.components.main

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

@Composable
fun AssetImage(
    path: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageBitmap = remember(path) {
        try {
            val inputStream: InputStream = context.assets.open(path)
            BitmapFactory.decodeStream(inputStream).asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    imageBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}
