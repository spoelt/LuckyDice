package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.PlayerInfo

@Composable
fun SelectedPlayer(
    modifier: Modifier = Modifier,
    playerInfo: PlayerInfo,
    onPointsChange: (Long, Pair<Int, String>) -> Unit,
    onTopPointsColumnPositioned: (Int) -> Unit,
    onBottomPointsColumnPositioned: (Int) -> Unit,
) {
    val points = remember(playerInfo) {
        playerInfo.columns.map { column ->
            column.points
        }
    }

    Column(modifier = modifier) {
        PlayerHeader(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                )
                .fillMaxWidth(),
            isSelected = true,
            name = playerInfo.name,
            onClick = {
                // do nothing
            }
        )

        TotalPlayerPoints(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
                .onGloballyPositioned { coordinates ->
                    onTopPointsColumnPositioned(coordinates.size.height)
                },
            totalPoints = points.map { map ->
                map.filterKeys { it <= 6 }
            },
            onPointsChange = { columnIndex, rowValuePair ->
                playerInfo.columns.getOrNull(columnIndex)?.columnId?.let { columnId ->
                    onPointsChange(columnId, rowValuePair)
                }
            },
            isSelected = true
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        )

        TotalPlayerPoints(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .onGloballyPositioned { coordinates ->
                    onBottomPointsColumnPositioned(coordinates.size.height)
                },
            totalPoints = points.map { map ->
                map.filterKeys { it > 6 }
            },
            onPointsChange = { columnIndex, rowValuePair ->
                playerInfo.columns.getOrNull(columnIndex)?.columnId?.let { columnId ->
                    onPointsChange(columnId, rowValuePair)
                }
            },
            isSelected = true
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface)
        )

        SumRow(
            modifier = Modifier.fillMaxWidth(),
            totalPoints = points
        )
    }
}
