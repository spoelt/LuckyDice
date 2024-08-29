package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.PlayerColumn

const val PLAYER_HEADER_HEIGHT = 48
const val TOTAL_PADDING_HEIGHT = 48

@Composable
fun PointsLegend(
    height: Int,
    elementHeight: Dp = TextFieldDefaults.MinHeight,
    items: List<Int> = getLegendItems()
) {
    val density = LocalDensity.current
    val columnHeight = remember(height) {
        with(density) {
            height
                .plus(PLAYER_HEADER_HEIGHT.dp.toPx())
                .plus(TOTAL_PADDING_HEIGHT.dp.toPx())
                .toDp()
        }
    }

    Column(
        modifier = Modifier
            .padding(end = 12.dp)
            .height(columnHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(PLAYER_HEADER_HEIGHT.dp))

        items.forEach { rowNumber ->
            Column(
                modifier = Modifier.height(elementHeight),
                verticalArrangement = Arrangement.Center
            ) {
                if (rowNumber < PlayerColumn.STREET) {
                    NumberedDice(rowNumber.toNumberedDiceType())
                } else {
                    TextDice(rowNumber.toTextDiceType())
                }
            }

            if (rowNumber == PlayerColumn.SIX) {
                Spacer(modifier = Modifier.height(16.dp))
            } else if (rowNumber != PlayerColumn.GRANDE) {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

private fun getLegendItems() = listOf(
    PlayerColumn.ONE,
    PlayerColumn.TWO,
    PlayerColumn.THREE,
    PlayerColumn.FOUR,
    PlayerColumn.FIVE,
    PlayerColumn.SIX,
    PlayerColumn.STREET,
    PlayerColumn.FULL_HOUSE,
    PlayerColumn.POKER,
    PlayerColumn.GRANDE,
)

private fun Int.toNumberedDiceType() = when (this) {
    PlayerColumn.ONE -> NumberedDiceType.ONE
    PlayerColumn.TWO -> NumberedDiceType.TWO
    PlayerColumn.THREE -> NumberedDiceType.THREE
    PlayerColumn.FOUR -> NumberedDiceType.FOUR
    PlayerColumn.FIVE -> NumberedDiceType.FIVE
    else -> NumberedDiceType.SIX
}

private fun Int.toTextDiceType() = when (this) {
    PlayerColumn.STREET -> TextDiceType.STREET
    PlayerColumn.FULL_HOUSE -> TextDiceType.FULL_HOUSE
    PlayerColumn.POKER -> TextDiceType.POKER
    else -> TextDiceType.GRANDE
}