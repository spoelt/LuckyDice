package com.spoelt.luckydice.presentation.selectgameoptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.luckydice.domain.DicePokerGameOptions
import com.spoelt.luckydice.domain.GameType
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectGameOptionsViewModel : ViewModel() {

    private val _gameOptions = MutableStateFlow(DicePokerGameOptions())
    val gameOptions = _gameOptions.asStateFlow()

    private val _gameType = MutableStateFlow<GameType?>(null)
    val gameType = _gameType.asStateFlow()

    private val _navigate = MutableSharedFlow<String>()
    val navigate = _navigate.asSharedFlow()

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
            val updatedPlayers = options.players?.toMutableMap()?.apply { put(index, name) }
            options.copy(players = updatedPlayers)
        }
    }

    fun updateNumberOfColumns(numberOfColumns: Int) {
        _gameOptions.update {
            it.copy(numberOfColumns = numberOfColumns)
        }
    }

    fun createGame() {
        // create game in background
        // if creation was success -> navigate

        viewModelScope.launch {
            _navigate.emit(NavigationRoutes.GameScreen.route)
        }
    }
}