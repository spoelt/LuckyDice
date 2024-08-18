package com.spoelt.luckydice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.spoelt.luckydice.domain.model.PlayerColumn

@Entity(
    tableName = "player_columns",
    foreignKeys = [
        ForeignKey(
            entity = PlayerInfoEntity::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlayerColumnEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "column_id")
    val columnId: Long = 0,

    @ColumnInfo(name = "player_id")
    val playerId: Long,

    @ColumnInfo(name = "column_number")
    val columnNumber: Int,

    @ColumnInfo(name = "points")
    val points: Int = 0
)

fun PlayerColumnEntity.toPlayerColumn() = PlayerColumn(
    columnId = this.columnId,
    playerId = this.playerId,
    columnNumber = this.columnNumber,
    points = this.points
)

fun List<PlayerColumnEntity>.toPlayerColumnList() = map { it.toPlayerColumn() }