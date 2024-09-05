package com.spoelt.luckydice.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.LuckyDiceNavigationTarget
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerInfo
import com.spoelt.luckydice.domain.model.PlayerPoints
import com.spoelt.luckydice.domain.repository.GameRepository
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import io.github.aakira.napier.Napier
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
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _game = MutableStateFlow<DicePokerGame?>(null)
    val game = _game.asStateFlow()

    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    val selectedPlayerId = _selectedPlayerId.asStateFlow()

    private val _snackbar = MutableSharedFlow<Boolean>()
    val snackbar = _snackbar.asSharedFlow()

    private val _navigateEvent = MutableSharedFlow<LuckyDiceNavigationTarget>()
    val navigateEvent = _navigateEvent.asSharedFlow()

    private var snackbarJob: Job? = null
    private var persistGameJob: Job? = null

    fun getGame(id: Long) {
        viewModelScope.launch {
            gameRepository.getDicePokerGame(id)?.let { game ->
                _game.value = game
                _selectedPlayerId.value = game.players.firstOrNull()?.id
            } ?: navigateBackToHome()
        }
    }

    private suspend fun navigateBackToHome() {
        _navigateEvent.emit(LuckyDiceNavigationTarget(route = NavigationRoutes.Home.route))
    }

    fun updateSelectedPlayer(id: Long) {
        _selectedPlayerId.update { id }
    }

    fun updatePoints(
        playerId: Long,
        columnId: Long,
        rowValuePair: Pair<Int, String>
    ) {
        val (indexToUpdate, inputValue) = rowValuePair
        val updatedValue = if (inputValue == "") {
            DEFAULT_VALUE
        } else {
            inputValue.toIntOrNull() ?: return
        }
        val currentGame = _game.value ?: return

        _game.update {
            val updatedPlayers = currentGame.players.map { player ->
                if (player.id == playerId) {
                    val updatedColumns = updatePlayerColumns(
                        columns = player.columns,
                        columnId = columnId,
                        updatedValue = updatedValue,
                        indexToUpdate = indexToUpdate
                    )
                    player.copy(columns = updatedColumns)
                } else {
                    player
                }
            }
            currentGame.copy(players = updatedPlayers)
        }
    }

    private fun updatePlayerColumns(
        columns: List<PlayerColumn>,
        columnId: Long,
        updatedValue: Int,
        indexToUpdate: Int
    ): List<PlayerColumn> {
        return columns.map { column ->
            if (column.columnId == columnId) {
                val updatedPoints = updatePlayerPoints(
                    points = column.points,
                    updatedValue = updatedValue,
                    indexToUpdate = indexToUpdate
                )
                column.copy(points = updatedPoints)
            } else {
                column
            }
        }
    }

    private fun updatePlayerPoints(
        points: Map<Int, PlayerPoints>,
        updatedValue: Int,
        indexToUpdate: Int
    ): Map<Int, PlayerPoints> {
        return points.toMutableMap().apply {
            val isError = arePointsInvalid(updatedValue, indexToUpdate)
            // could this be called somewhere else?
            handleSnackbar(isError)

            this[indexToUpdate] = PlayerPoints(
                pointsValue = updatedValue,
                error = isError,
                pointId = this[indexToUpdate]!!.pointId
            )
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
        return if (value == DEFAULT_VALUE || value == 0) {
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

    fun finishGame() {
        viewModelScope.launch {
            _game.value?.let { game ->
                val endOfGamePoints = getEndOfGamePoints(game.players)
                val insertedRows = gameRepository.updatePoints(endOfGamePoints)

                val home = NavigationRoutes.Home.route
                val hasInsertErrors = insertedRows.any { it == -1L }
                val route = if (hasInsertErrors) {
                    Napier.e(message = "Error updating points rows")
                    home
                } else {
                    NavigationRoutes.ResultsScreen.createRoute(game.id)
                }

                val target = LuckyDiceNavigationTarget(
                    route = route,
                    popUpToRoute = home,
                    inclusive = hasInsertErrors
                )
                _navigateEvent.emit(target)
            }
        }
    }

    /**
     * If fields haven't been filled but the game should be ended, change all non-filled points
     * fields from -1 to 0.
     */
    private fun getEndOfGamePoints(players: List<PlayerInfo>) = players.flatMap { player ->
        player.columns.map { column ->
            val updatedPoints = column.points.mapValues { (_, point) ->
                if (point.pointsValue == -1) {
                    point.copy(pointsValue = 0)
                } else {
                    point
                }
            }
            column.copy(points = updatedPoints)
        }
    }

    fun persistGamePeriodically(interval: Long = AUTO_SAVE_INTERVAL) {
        cancelPersistGameJob()
        persistGameJob = viewModelScope.launch {
            while (isActive) {
                delay(interval)
                _game.value?.let { game ->
                    persistGame(game)
                }
            }
        }
    }

    private suspend fun persistGame(game: DicePokerGame) {
        val points = game.players.flatMap { player ->
            player.columns
        }
        val insertedRows = gameRepository.updatePoints(points)
        val hasInsertErrors = insertedRows.any { it == -1L }
        if (hasInsertErrors) {
            Napier.e("Failed to persist game")
        }
    }

    fun cancelPersistGameJob() {
        persistGameJob?.cancel()
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
        const val AUTO_SAVE_INTERVAL = 60000L
    }
}