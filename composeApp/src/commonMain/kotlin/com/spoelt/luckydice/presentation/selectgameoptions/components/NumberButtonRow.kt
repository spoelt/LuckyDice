package com.spoelt.luckydice.presentation.selectgameoptions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun NumberButtonRow(
    range: IntRange,
    selectedNumber: Int,
    onClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        range.forEach { i ->
            NumberButton(
                number = i,
                isSelected = i == selectedNumber,
                onClick = onClick
            )
        }
    }
}