package com.spoelt.luckydice.presentation.game

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.PlayerInfo
import com.spoelt.luckydice.presentation.game.components.PointsLegend
import com.spoelt.luckydice.presentation.game.components.SelectedPlayer
import com.spoelt.luckydice.presentation.game.components.UnselectedPlayer
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.end_game
import org.jetbrains.compose.resources.stringResource

/**
 * Composable function that renders the game screen, displaying players and their respective points
 * columns.
 *
 * @param modifier Modifier to be applied to the root composable.
 * @param playerInfos List of [PlayerInfo] objects, each representing a player with their respective
 * columns.
 * @param selectedPlayerId ID of the currently selected player. Used to highlight the selected
 * player's column.
 * @param onSelectedPlayerClick Callback function to be invoked when a player is selected.
 * Takes the player's ID as a parameter.
 * @param onPointsChange Callback function to be invoked when the points in a player's column are
 * updated. Takes four parameters:
 * - playerId: ID of the player whose points are being updated.
 * - columnId: The ID of the column being updated.
 * - rowIndex: The index of the row within the column that is being updated.
 * - value: The new value to be set for the specified row.
 * @param onStopGameClick Callback function to be invoked when a user ends the current game.
 */
@Composable
fun Game(
    modifier: Modifier = Modifier,
    playerInfos: List<PlayerInfo>,
    selectedPlayerId: Long?,
    onSelectedPlayerClick: (Long) -> Unit,
    onPointsChange: (Long, Long, Pair<Int, String>) -> Unit,
    onStopGameClick: () -> Unit
) {
    val columnWidths = remember(playerInfos, selectedPlayerId) {
        playerInfos.associate { player ->
            val singleColumnWidth = if (player.id == selectedPlayerId) 70 else 50
            val numberOfColumns = player.columns.size
            val totalWidth = if (numberOfColumns > 1) {
                (numberOfColumns * singleColumnWidth).dp
            } else if (player.id == selectedPlayerId) {
                120.dp
            } else {
                100.dp
            }
            player.id to totalWidth
        }
    }

    var topPointsColumnHeight by remember { mutableStateOf(0) }
    var bottomPointsColumnHeight by remember { mutableStateOf(0) }

    Scaffold(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
        ) {
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    item {
                        PointsLegend(
                            height = topPointsColumnHeight + bottomPointsColumnHeight
                        )
                    }

                    playerInfos.sortedBy { it.id }.forEachIndexed { index, player ->
                        item {
                            val width = columnWidths[player.id] ?: 100.dp
                            if (player.id == selectedPlayerId) {
                                SelectedPlayer(
                                    modifier = Modifier.width(width),
                                    playerInfo = player,
                                    onPointsChange = { columnId, valuePair ->
                                        onPointsChange(player.id, columnId, valuePair)
                                    },
                                    onTopPointsColumnPositioned = { heightInPx ->
                                        topPointsColumnHeight = heightInPx
                                    },
                                    onBottomPointsColumnPositioned = { heightInPx ->
                                        bottomPointsColumnHeight = heightInPx
                                    },
                                )
                            } else {
                                UnselectedPlayer(
                                    modifier = Modifier.width(width),
                                    playerInfo = player,
                                    onClick = { onSelectedPlayerClick(player.id) },
                                    topPointsColumnHeight = topPointsColumnHeight,
                                    bottomPointsColumnHeight = bottomPointsColumnHeight
                                )
                            }
                        }

                        if (index != playerInfos.size) {
                            item {
                                Spacer(modifier = Modifier.width(width = 8.dp))
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 8.dp),
                    onClick = onStopGameClick
                ) {
                    Text(
                        text = stringResource(Res.string.end_game),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
