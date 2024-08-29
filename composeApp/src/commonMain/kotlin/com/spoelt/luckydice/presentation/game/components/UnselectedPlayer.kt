package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.PlayerInfo

@Composable
fun UnselectedPlayer(
    modifier: Modifier = Modifier,
    playerInfo: PlayerInfo,
    onClick: () -> Unit,
    topPointsColumnHeight: Int,
    bottomPointsColumnHeight: Int,
) {
    val points = remember(playerInfo) {
        playerInfo.columns.map { column ->
            column.points
        }
    }
    val density = LocalDensity.current
    val topColumnHeight = remember(topPointsColumnHeight) {
        with(density) { topPointsColumnHeight.toFloat().toDp() }
    }
    val bottomColumnHeight = remember(bottomPointsColumnHeight) {
        with(density) { bottomPointsColumnHeight.toFloat().toDp() }
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
            isSelected = false,
            name = playerInfo.name,
            onClick = onClick
        )

        TotalPlayerPoints(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onClick() }
                .height(topColumnHeight),
            totalPoints = points.map { map ->
                map.filterKeys { it <= 6 }
            },
            isSelected = false,
            onPointsChange = { _, _ ->
                // do nothing
            }
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .height(2.dp)
        )

        TotalPlayerPoints(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onClick() }
                .height(bottomColumnHeight),
            totalPoints = points.map { map ->
                map.filterKeys { it > 6 }
            },
            isSelected = false,
            onPointsChange = { _, _ ->
                // do nothing
            }
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
