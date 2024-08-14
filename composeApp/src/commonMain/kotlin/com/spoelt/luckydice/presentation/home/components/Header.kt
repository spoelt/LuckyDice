package com.spoelt.luckydice.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.home_screen_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun Header(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(Res.string.home_screen_title),
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
}