package com.spoelt.luckydice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.spoelt.luckydice.data.model.DicePokerGameCreationEntity
import com.spoelt.luckydice.data.model.GameWithPlayersAndColumns
import com.spoelt.luckydice.data.model.PlayerColumnEntity
import com.spoelt.luckydice.data.model.PlayerInfoEntity
import com.spoelt.luckydice.data.model.PlayerPointsEntity
import com.spoelt.luckydice.data.model.toDicePokerGameCreationEntity
import com.spoelt.luckydice.domain.model.DicePokerGameCreation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

@Dao
interface LuckyDiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDicePokerGame(game: DicePokerGameCreationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDicePokerPlayers(players: List<PlayerInfoEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDicePokerPlayerColumnInfo(columns: List<PlayerColumnEntity>): List<Long>

    @Upsert
    suspend fun upsertPlayerPoints(playerPoints: List<PlayerPointsEntity>)

    @Transaction
    @Query("SELECT * FROM dice_poker_games WHERE game_id = :gameId")
    suspend fun getDicePokerGameWithPlayers(gameId: Long): GameWithPlayersAndColumns?

    @Transaction
    suspend fun createDicePokerGame(game: DicePokerGameCreation): Long {
        return withContext(Dispatchers.IO) {
            val gameId = insertDicePokerGame(game.toDicePokerGameCreationEntity())

            game.players?.let { players ->
                val playersToInsert = players.map { (_, name) ->
                    PlayerInfoEntity(
                        gameId = gameId,
                        name = name
                    )
                }
                val playerIds = insertDicePokerPlayers(playersToInsert)

                val playerColumnsToInsert = playerIds.flatMap { playerId ->
                    (1..game.numberOfColumns).map { columnNumber ->
                        PlayerColumnEntity(
                            playerId = playerId,
                            columnNumber = columnNumber
                        )
                    }
                }
                val columnIds = insertDicePokerPlayerColumnInfo(playerColumnsToInsert)

                val playerColumnPointsToInsert = columnIds.flatMap { columnId ->
                    generateDefaultPoints().map { (rowIndex, points) ->
                        PlayerPointsEntity(
                            columnId = columnId,
                            rowIndex = rowIndex,
                            points = points
                        )
                    }
                }

                upsertPlayerPoints(playerColumnPointsToInsert)
            }

            gameId
        }
    }

    // Helper function to generate the default points map
    private fun generateDefaultPoints(): Map<Int, Int> = mapOf(
        PlayerPointsEntity.ONE to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.TWO to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.THREE to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.FOUR to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.FIVE to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.SIX to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.STREET to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.FULL_HOUSE to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.POKER to DEFAULT_POINTS_VALUE,
        PlayerPointsEntity.GRANDE to DEFAULT_POINTS_VALUE
    )

    companion object {
        const val DEFAULT_POINTS_VALUE = -1
    }
}