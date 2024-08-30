package com.spoelt.luckydice.data.repository

import com.spoelt.luckydice.data.local.db.LuckyDiceDatabase
import com.spoelt.luckydice.data.model.DicePokerGameCreationEntity
import com.spoelt.luckydice.data.model.GameWithPlayersAndColumns
import com.spoelt.luckydice.data.model.PlayerPointsEntity
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerInfo
import com.spoelt.luckydice.domain.model.PlayerPoints
import com.spoelt.luckydice.domain.repository.GameRepository

class GameRepositoryImpl(private val database: LuckyDiceDatabase) : GameRepository {
    override suspend fun createDicePokerGame(game: DicePokerGameCreation): Long =
        database.getDicePokerGameDao().createDicePokerGame(
            game = game.toDicePokerGameCreationEntity(),
            players = game.players
        )

    override suspend fun getDicePokerGame(gameId: Long): DicePokerGame? =
        database.getDicePokerGameDao().getDicePokerGameWithPlayers(gameId)?.toDicePokerGame()

    override suspend fun updatePoints(columns: List<PlayerColumn>): List<Long> {
        return database.getDicePokerGameDao().insertPlayerPoints(columns.toPlayerPointsEntities())
    }

    private fun DicePokerGameCreation.toDicePokerGameCreationEntity() = DicePokerGameCreationEntity(
        numberOfPlayers = this.numberOfPlayers,
        numberOfColumns = this.numberOfColumns
    )

    private fun GameWithPlayersAndColumns.toDicePokerGame(): DicePokerGame {
        return DicePokerGame(
            numberOfPlayers = game.numberOfPlayers,
            numberOfColumns = game.numberOfColumns,
            players = playersWithColumns.map { playerWithColumns ->
                PlayerInfo(
                    id = playerWithColumns.player.playerId,
                    name = playerWithColumns.player.name,
                    columns = playerWithColumns.columns.map { columnWithPoints ->
                        PlayerColumn(
                            columnId = columnWithPoints.playerColumn.columnId,
                            columnNumber = columnWithPoints.playerColumn.columnNumber,
                            points = columnWithPoints.points.associate {
                                it.rowIndex to PlayerPoints(
                                    pointsValue = it.points,
                                    pointId = it.pointId
                                )
                            },
                        )
                    }
                )
            },
            id = game.gameId
        )
    }

    private fun List<PlayerColumn>.toPlayerPointsEntities() = flatMap { it.toPlayerPointsEntity() }

    private fun PlayerColumn.toPlayerPointsEntity() = this.points.map { (index, playerPoints) ->
        PlayerPointsEntity(
            pointId = playerPoints.pointId,
            columnId = this.columnId,
            rowIndex = index,
            points = playerPoints.pointsValue
        )
    }
}