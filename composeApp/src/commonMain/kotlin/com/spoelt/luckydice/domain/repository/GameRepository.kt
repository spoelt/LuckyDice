package com.spoelt.luckydice.domain.repository

import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation
import com.spoelt.luckydice.domain.model.PlayerColumn

interface GameRepository {
    suspend fun createDicePokerGame(game: DicePokerGameCreation): Long

    suspend fun getDicePokerGame(gameId: Long): DicePokerGame?

    suspend fun updatePoints(columns: List<PlayerColumn>): List<Long>
}