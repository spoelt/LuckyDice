package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.theme.primaryGradient

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    name: String,
    onClick: () -> Unit,
) {
    val textColor = if (isSelected) {
        Color.Black
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val backgroundModifier = Modifier.then(
        if (isSelected) {
            Modifier.background(primaryGradient)
        } else {
            Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        }
    )

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp
                )
            )
            .then(backgroundModifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .clickable { onClick() }
                .padding(12.dp),
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}