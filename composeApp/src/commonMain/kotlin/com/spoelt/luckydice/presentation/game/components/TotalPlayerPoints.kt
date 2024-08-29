package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.PlayerPoints

@Composable
fun TotalPlayerPoints(
    modifier: Modifier = Modifier,
    totalPoints: List<Map<Int, PlayerPoints>>,
    onPointsChange: (Int, Pair<Int, String>) -> Unit,
    isSelected: Boolean
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        totalPoints.forEachIndexed { index, points ->
            PlayerPointsColumn(
                modifier = Modifier.weight(1f),
                points = points,
                onPointsChange = { valuePair ->
                    if (isSelected) {
                        onPointsChange(index, valuePair)
                    } else {
                        // do nothing
                    }
                },
                isSelected = isSelected
            )
        }
    }
}
