package com.spoelt.luckydice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.spoelt.luckydice.domain.model.PlayerInfo

@Entity(
    tableName = "players",
    foreignKeys = [
        ForeignKey(
            entity = DicePokerGameEntity::class,
            parentColumns = ["game_id"],
            childColumns = ["game_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlayerInfoEntity(
    @ColumnInfo(name = "player_id")
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0,

    @ColumnInfo(name = "game_id")
    val gameId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "points")
    val points: Int = 0
)

fun PlayerInfoEntity.toPlayerInfo() = PlayerInfo(
    id = this.playerId,
    name = this.name,
    points = this.points
)