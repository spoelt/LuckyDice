package com.spoelt.luckydice.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.spoelt.luckydice.data.model.DicePokerGameEntity
import com.spoelt.luckydice.data.model.GameWithPlayers
import com.spoelt.luckydice.data.model.PlayerInfoEntity
import com.spoelt.luckydice.data.model.toDicePokerGameEntity
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation

@Dao
interface LuckyDiceDao {
    @Upsert
    suspend fun upsertDicePokerGame(game: DicePokerGameEntity): Long

    @Upsert
    suspend fun upsertDicePokerPlayers(players: List<PlayerInfoEntity>)

    @Transaction
    @Query("SELECT * FROM dice_poker_games WHERE game_id = :gameId")
    suspend fun getDicePokerGameWithPlayers(gameId: Long): GameWithPlayers

    @Transaction
    suspend fun createDicePokerGameWithPlayers(game: DicePokerGameCreation): Long {
        val gameId = upsertDicePokerGame(game.toDicePokerGameEntity())

        game.players?.let { players ->
            val playerEntities = players.map { (_, name) ->
                PlayerInfoEntity(
                    gameId = gameId,
                    name = name,
                )
            }
            upsertDicePokerPlayers(playerEntities)
        }

        return gameId
    }

    @Transaction
    suspend fun updateDicePokerGameWithPlayers(game: DicePokerGame) {
        val gameId = upsertDicePokerGame(game.toDicePokerGameEntity())

        val playerEntities = game.players.map { (_, player) ->
            PlayerInfoEntity(
                gameId = gameId,
                name = player.name,
                playerId = player.id,
                points = player.points
            )
        }
        upsertDicePokerPlayers(playerEntities)
    }
}