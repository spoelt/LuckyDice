package com.spoelt.luckydice.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerPoints
import com.spoelt.luckydice.domain.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _game = MutableStateFlow<DicePokerGame?>(null)
    val game = _game.asStateFlow()

    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    val selectedPlayerId = _selectedPlayerId.asStateFlow()

    private val _snackbar = MutableSharedFlow<Boolean>()
    val snackbar = _snackbar.asSharedFlow()

    private var snackbarJob: Job? = null

    fun getGame(id: Long) {
        viewModelScope.launch {
            gameRepository.getDicePokerGame(id)?.let { game ->
                _game.value = game
                _selectedPlayerId.value = game.players.firstOrNull()?.id
            }
        }
    }

    fun updateSelectedPlayer(id: Long) {
        _selectedPlayerId.update { id }
    }

    fun updatePoints(
        playerId: Long,
        columnId: Long,
        rowValuePair: Pair<Int, String>
    ) {
        val indexToUpdate = rowValuePair.first
        val updatedValue = if (rowValuePair.second == "") {
            DEFAULT_VALUE
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
                                val isError = arePointsInvalid(updatedValue, indexToUpdate)
                                handleSnackbar(isError)

                                this[indexToUpdate] = PlayerPoints(
                                    pointsValue = updatedValue,
                                    error = isError
                                )
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

    private fun handleSnackbar(isError: Boolean) {
        if (isError) displayDebouncedSnackbar() else cancelSnackbarJob()
    }

    private fun displayDebouncedSnackbar() {
        cancelSnackbarJob()
        snackbarJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY)
            if (isActive) {
                _snackbar.emit(true)
            }
        }
    }

    private fun cancelSnackbarJob() {
        snackbarJob?.cancel()
    }

    private fun arePointsInvalid(value: Int, indexToUpdate: Int): Boolean {
        // if the field is empty, we'll count it as a valid input
        return if (value == DEFAULT_VALUE) {
            false
        } else if (indexToUpdate <= PlayerColumn.SIX) {
            // value must be a multiple of the row and smaller or equal to the max value you can
            // achieve in that row
            value % indexToUpdate != 0 || value > indexToUpdate.times(NUMBER_OF_DICE)
        } else {
            when (indexToUpdate) {
                PlayerColumn.STREET -> value != STREET_POINTS && value != STREET_POINTS_SERVED
                PlayerColumn.FULL_HOUSE -> value != FULL_HOUSE_POINTS && value != FULL_HOUSE_POINTS_SERVED
                PlayerColumn.POKER -> value != POKER_POINTS && value != POKER_POINTS_SERVED
                else -> value != GRANDE_VALUE && value != GRANDE_VALUE_SERVED
            }
        }
    }

    companion object {
        const val NUMBER_OF_DICE = 5
        const val DEFAULT_VALUE = -1
        const val STREET_POINTS = 20
        const val STREET_POINTS_SERVED = 25
        const val FULL_HOUSE_POINTS = 30
        const val FULL_HOUSE_POINTS_SERVED = 35
        const val POKER_POINTS = 40
        const val POKER_POINTS_SERVED = 45
        const val GRANDE_VALUE = 50
        const val GRANDE_VALUE_SERVED = 100
        const val DEBOUNCE_DELAY = 1000L
    }
}