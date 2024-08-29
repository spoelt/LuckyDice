package com.spoelt.luckydice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "player_columns",
    foreignKeys = [
        ForeignKey(
            entity = PlayerInfoEntity::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("player_id")]
)
data class PlayerColumnEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "column_id")
    val columnId: Long = 0,

    @ColumnInfo(name = "player_id")
    val playerId: Long,

    @ColumnInfo(name = "column_number")
    val columnNumber: Int,
)