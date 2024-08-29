package com.spoelt.luckydice.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _game = MutableStateFlow<DicePokerGame?>(null)
    val game = _game.asStateFlow()

    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    val selectedPlayerId = _selectedPlayerId.asStateFlow()

    fun getGame(id: Long) {
        viewModelScope.launch {
            gameRepository.getDicePokerGame(id)?.let { game ->
                _game.value = game
                _selectedPlayerId.value = game.players.firstOrNull()?.id
            }
        }
    }

    fun updateSelectedPlayer(id: Long) {
        _selectedPlayerId.update {
            id
        }
    }

    fun updatePoints(
        playerId: Long,
        columnId: Long,
        rowValuePair: Pair<Int, String>
    ) {
        val indexToUpdate = rowValuePair.first
        val updatedValue = if (rowValuePair.second == "") {
            -1
        } else {
            rowValuePair.second.toIntOrNull() ?: return
        }
        val currentGame = _game.value ?: return

        _game.update {
            val updatedPlayers = currentGame.players.map { player ->
                if (player.id == playerId) {
                    val updatedColumns = player.columns.map { column ->
                        if (column.columnId == columnId) {
                            val updatedPoints = column.points.toMutableMap().apply {
                                this[indexToUpdate] = updatedValue
                            }
                            column.copy(points = updatedPoints)
                        } else {
                            column
                        }
                    }
                    player.copy(columns = updatedColumns)
                } else {
                    player
                }
            }
            currentGame.copy(players = updatedPlayers)
        }
    }
}