package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SumRow(
    modifier: Modifier = Modifier,
    totalPoints: List<Map<Int, Int>>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        totalPoints.forEach { pointsPerColumn ->
            Sum(
                modifier = Modifier.weight(1f),
                points = pointsPerColumn
            )
        }
    }
}

@Composable
fun Sum(
    modifier: Modifier = Modifier,
    points: Map<Int, Int>
) {
    val sum = remember(points) {
        points.values.filter { it != -1 }.sum()
    }
    val displaySum = remember(sum) {
        "$sum"
    }

    Text(
        modifier = modifier,
        text = displaySum,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall
    )
}