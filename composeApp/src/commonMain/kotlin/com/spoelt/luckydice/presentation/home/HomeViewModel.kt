package com.spoelt.luckydice.presentation.home

import androidx.lifecycle.ViewModel
import com.spoelt.luckydice.domain.GameType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _gameTypes = MutableStateFlow(getAvailableGameTypes())
    val gameTypes = _gameTypes.asStateFlow()

    private fun getAvailableGameTypes() = listOf(
        GameType.DICE_POKER,
        GameType.YAHTZEE,
        GameType.FARKLES
    )

}