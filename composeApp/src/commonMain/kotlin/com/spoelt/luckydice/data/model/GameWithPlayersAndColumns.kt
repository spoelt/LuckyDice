package com.spoelt.luckydice.data.model

import androidx.room.Embedded
import androidx.room.Relation

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
