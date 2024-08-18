package com.spoelt.luckydice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.spoelt.luckydice.data.model.DicePokerGameEntity
import com.spoelt.luckydice.data.model.GameWithPlayersAndColumns
import com.spoelt.luckydice.data.model.PlayerColumnEntity
import com.spoelt.luckydice.data.model.PlayerInfoEntity
import com.spoelt.luckydice.data.model.toDicePokerGameEntity
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation

@Dao
interface LuckyDiceDao {
    @Upsert
    suspend fun upsertDicePokerGame(game: DicePokerGameEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDicePokerPlayers(players: List<PlayerInfoEntity>): List<Long>

    @Update
    suspend fun updateDicePokerPlayers(players: List<PlayerInfoEntity>)

    @Upsert
    suspend fun upsertDicePokerPlayerColumnInfo(columns: List<PlayerColumnEntity>)

    @Transaction
    @Query("SELECT * FROM dice_poker_games WHERE game_id = :gameId")
    suspend fun getDicePokerGameWithPlayers(gameId: Long): GameWithPlayersAndColumns

    @Transaction
    suspend fun createDicePokerGameWithPlayers(game: DicePokerGameCreation): Long {
        val gameId = upsertDicePokerGame(game.toDicePokerGameEntity())

        game.players?.let { players ->
            val newPlayerEntities  = players.map { (_, name) ->
                PlayerInfoEntity(
                    gameId = gameId,
                    name = name,
                )
            }

            val playerIds = insertDicePokerPlayers(newPlayerEntities)

            val playerColumns = playerIds.flatMap { playerId ->
                (1..game.numberOfColumns).map { columnNumber ->
                    PlayerColumnEntity(
                        playerId = playerId,
                        columnNumber = columnNumber,
                        points = 0 // Initialize points
                    )
                }
            }
            upsertDicePokerPlayerColumnInfo(playerColumns)
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
            )
        }
        updateDicePokerPlayers(playerEntities)

        val playerColumns = game.players.flatMap { (_, player) ->
            player.columns.map { column ->
                PlayerColumnEntity(
                    playerId = player.id,
                    columnNumber = column.columnNumber,
                    points = column.points
                )
            }
        }
        upsertDicePokerPlayerColumnInfo(playerColumns)
    }
}