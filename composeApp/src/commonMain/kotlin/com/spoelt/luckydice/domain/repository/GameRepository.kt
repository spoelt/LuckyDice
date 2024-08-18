package com.spoelt.luckydice.domain.repository

import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation

interface GameRepository {
    suspend fun createDicePokerGame(game: DicePokerGameCreation): Long

    suspend fun updateDicePokerGame(game: DicePokerGame)

    suspend fun getDicePokerGame(gameId: Long): DicePokerGame
}