package com.spoelt.luckydice.presentation.selectgameoptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.luckydice.domain.model.DicePokerGameCreation
import com.spoelt.luckydice.domain.model.GameType
import com.spoelt.luckydice.domain.model.LuckyDiceNavigationTarget
import com.spoelt.luckydice.domain.repository.GameRepository
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectGameOptionsViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _gameOptions = MutableStateFlow(DicePokerGameCreation())
    val gameOptions = _gameOptions.asStateFlow()

    private val _gameType = MutableStateFlow<GameType?>(null)
    val gameType = _gameType.asStateFlow()

    private val _navigateEvent = MutableSharedFlow<LuckyDiceNavigationTarget>()
    val navigateEvent = _navigateEvent.asSharedFlow()

    // TODO: adapt later when more games available
    /*fun initViewModel(type: GameType) {
        when (type) {
            GameType.DICE_POKER -> {
                _gameOptions.update {
                    DicePokerGameOptions()
                }
            }

            GameType.YAHTZEE -> TODO()
            GameType.FARKLES -> TODO()
        }
    }*/

    fun setGameType(type: GameType) {
        _gameType.update { type }
    }

    fun setNumberOfPlayers(numberOfPlayers: Int) {
        _gameOptions.update {
            it.copy(numberOfPlayers = numberOfPlayers)
        }
    }

    fun initializePlayersMap() {
        val numberOfPlayers = _gameOptions.value.numberOfPlayers
        val players = (0 until numberOfPlayers).associateWith { "" }
        _gameOptions.update {
            it.copy(players = players)
        }
    }

    fun updatePlayerName(index: Int, name: String) {
        _gameOptions.update { options ->
            val updatedPlayers = options.players
                ?.toMutableMap()
                ?.apply {
                    put(index, name)
                }
            options.copy(players = updatedPlayers)
        }
    }

    fun updateNumberOfColumns(numberOfColumns: Int) {
        _gameOptions.update {
            it.copy(numberOfColumns = numberOfColumns)
        }
    }

    fun createGame() {
        viewModelScope.launch {
            val updatedPlayers = trimPlayerNames(_gameOptions.value.players)
            val updatedGame = _gameOptions.value.copy(players = updatedPlayers)
            val gameId = gameRepository.createDicePokerGame(updatedGame)
            val navigationTarget = LuckyDiceNavigationTarget(
                route = NavigationRoutes.GameScreen.createRoute(gameId),
                popUpToRoute = NavigationRoutes.Home.route,
            )
            _navigateEvent.emit(navigationTarget)
        }
    }

    private fun trimPlayerNames(players: Map<Int, String>?): Map<Int, String>? {
        return players?.mapValues { (_, name) ->
            name.trim()
        }
    }

    fun reset() {
        _gameOptions.update { DicePokerGameCreation() }
        _gameType.update { null }
    }
}