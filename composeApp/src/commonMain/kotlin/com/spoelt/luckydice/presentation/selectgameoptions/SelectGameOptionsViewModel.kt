package com.spoelt.luckydice.presentation.selectgameoptions

import androidx.lifecycle.ViewModel
import com.spoelt.luckydice.domain.DicePokerGameOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectGameOptionsViewModel : ViewModel() {

    private val _gameOptions = MutableStateFlow(DicePokerGameOptions())
    val gameOptions = _gameOptions.asStateFlow()

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
}