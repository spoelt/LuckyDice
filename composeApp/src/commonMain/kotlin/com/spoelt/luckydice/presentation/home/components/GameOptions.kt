package com.spoelt.luckydice.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.spoelt.luckydice.domain.GameType
import com.spoelt.luckydice.presentation.util.getScreenWidth
import kotlin.math.floor

const val GAME_OPTION_PADDING = 6

@Composable
fun GameOptions(
    modifier: Modifier = Modifier,
    gameTypes: List<GameType>,
    onGameTypeSelected: (GameType) -> Unit
) {
    val screenWidth = getScreenWidth()
    val maxItemsPerRow = remember(screenWidth) {
        calculateMaxItemsPerRow(screenWidth - 2 * 16.dp) // subtract outer padding
    }
    val chunkedGameTypes = remember(gameTypes, maxItemsPerRow) {
        gameTypes.chunked(maxItemsPerRow)
    }

    chunkedGameTypes.forEachIndexed { index, chunkedList ->
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(GAME_OPTION_PADDING.dp),
        ) {
            chunkedList.forEach { type ->
                GameOption(
                    type = type,
                    onGameTypeSelected = onGameTypeSelected
                )
            }
        }

        if (index != chunkedGameTypes.size) {
            Spacer(modifier = Modifier.height(GAME_OPTION_PADDING.dp))
        }
    }
}

fun calculateMaxItemsPerRow(
    screenWidth: Dp,
    itemSize: Dp = CARD_SIZE.dp + GAME_OPTION_PADDING.dp
): Int {
    return floor(screenWidth / itemSize).toInt()
}