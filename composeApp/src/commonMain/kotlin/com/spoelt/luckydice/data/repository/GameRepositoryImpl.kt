package com.spoelt.luckydice.data.repository

import com.spoelt.luckydice.data.local.db.LuckyDiceDatabase
import com.spoelt.luckydice.data.model.toDicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation
import com.spoelt.luckydice.domain.repository.GameRepository

class GameRepositoryImpl(private val database: LuckyDiceDatabase) : GameRepository {
    override suspend fun createDicePokerGame(game: DicePokerGameCreation): Long =
        database.getDicePokerGameDao().createDicePokerGameWithPlayers(game)

    override suspend fun updateDicePokerGame(game: DicePokerGame) {
        database.getDicePokerGameDao().updateDicePokerGameWithPlayers(game)
    }

    override suspend fun getDicePokerGame(gameId: Long): DicePokerGame =
        database.getDicePokerGameDao().getDicePokerGameWithPlayers(gameId).toDicePokerGame()

}