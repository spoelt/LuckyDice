package com.spoelt.luckydice.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerInfo

data class GameWithPlayersAndColumns(
    @Embedded val game: DicePokerGameCreationEntity,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "game_id",
        entity = PlayerInfoEntity::class
    )
    val playersWithColumns: List<PlayerWithColumns>
)

data class PlayerWithColumns(
    @Embedded val player: PlayerInfoEntity,
    @Relation(
        parentColumn = "player_id",
        entityColumn = "player_id",
        entity = PlayerColumnEntity::class
    )
    val columns: List<PlayerColumnWithPoints>
)

data class PlayerColumnWithPoints(
    @Embedded val playerColumn: PlayerColumnEntity,
    @Relation(
        parentColumn = "column_id",
        entityColumn = "column_id",
        entity = PlayerPointsEntity::class
    )
    val points: List<PlayerPointsEntity>
)

fun GameWithPlayersAndColumns.toDicePokerGame(): DicePokerGame {
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
                        points = columnWithPoints.points.associate { it.rowIndex to it.points },
                    )
                }
            )
        }
    )
}