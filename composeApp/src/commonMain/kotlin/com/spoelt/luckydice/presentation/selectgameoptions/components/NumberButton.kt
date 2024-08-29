package com.spoelt.luckydice.presentation.selectgameoptions.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.theme.primaryGradient

@Composable
fun NumberButton(
    number: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    Button(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(primaryGradient),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        onClick = { onClick(number) },
        border = BorderStroke(
            width = if (isSelected) 4.dp else 0.dp,
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "$number",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
    }
}