package com.spoelt.luckydice.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerRanking
import com.spoelt.luckydice.domain.model.PlayerResult
import com.spoelt.luckydice.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerResult>>(emptyList())
    val players = _players.asStateFlow()

    fun getPlayerPoints(gameId: Long) {
        viewModelScope.launch {
            gameRepository.getDicePokerGame(gameId)?.let { game ->
                val playerPointsMap = calculatePlayerPoints(game)
                val sortedPlayers = createRankedPlayerResults(game, playerPointsMap)
                _players.update { sortedPlayers }
            }
        }
    }

    private fun calculatePlayerPoints(game: DicePokerGame): Map<Long, Int> {
        val playerPointsMap = game.players.associate { it.id to 0 }.toMutableMap()

        for (columnNumber in 1..game.numberOfColumns) {
            // Calculate points for each player in this column
            val columnPoints = game.players.associate { player ->
                player.id to (player.columns.find { it.columnNumber == columnNumber }
                    ?.points?.values?.sumOf { it.pointsValue } ?: 0)
            }

            // Find the maximum points in this column
            val maxPoints = columnPoints.values.maxOrNull() ?: 0

            // Find all players with the maximum points (winners of this column)
            val columnWinners = columnPoints.filter { it.value == maxPoints }.keys

            // Distribute points to all winners
            columnWinners.forEach { winnerId ->
                playerPointsMap[winnerId] = if (game.numberOfColumns > 2) {
                    playerPointsMap.get(winnerId)?.plus(columnNumber) ?: columnNumber
                } else playerPointsMap.get(winnerId)?.plus(1) ?: 1
            }
        }

        return playerPointsMap
    }

    private fun createRankedPlayerResults(
        game: DicePokerGame,
        playerPointsMap: Map<Long, Int>
    ): List<PlayerResult> {
        val sortedEntries = playerPointsMap.entries.sortedByDescending { it.value }
        val groupedByPoints = sortedEntries.groupBy { it.value }
        val uniquePointValues = groupedByPoints.keys.sorted().reversed()

        return sortedEntries.map { (playerId, points) ->
            val ranking = when (uniquePointValues.indexOf(points)) {
                0 -> PlayerRanking.FIRST_PLACE
                1 -> PlayerRanking.SECOND_PLACE
                2 -> PlayerRanking.THIRD_PLACE
                else -> PlayerRanking.OTHER
            }

            PlayerResult(
                name = game.players.first { it.id == playerId }.name,
                finalPoints = points,
                ranking = ranking
            )
        }
    }
}