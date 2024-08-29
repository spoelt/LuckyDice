package com.spoelt.luckydice.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                val sortedPlayers = game.players.map { player ->
                    val totalPoints = player.columns.sumOf { column ->
                        column.points.values.sumOf { playerPoints ->
                            if (playerPoints.pointsValue != -1) playerPoints.pointsValue else 0
                        }
                    }
                    PlayerResult(
                        name = player.name,
                        totalPoints = totalPoints
                    )
                }.sortedByDescending { it.totalPoints }

                _players.update { sortedPlayers }
            }
        }
    }

}