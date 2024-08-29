package com.spoelt.luckydice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "player_points",
    foreignKeys = [
        ForeignKey(
            entity = PlayerColumnEntity::class,
            parentColumns = ["column_id"],
            childColumns = ["column_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("column_id")]
)
data class PlayerPointsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "point_id")
    val pointId: Long = 0,

    @ColumnInfo(name = "column_id")
    val columnId: Long,

    @ColumnInfo(name = "row_index")
    val rowIndex: Int,

    @ColumnInfo(name = "points")
    val points: Int
) {
    companion object {
        // The following constants are used for [rowIndex] and correspond to keys in the points map.
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SIX = 6
        const val STREET = 7
        const val FULL_HOUSE = 8
        const val POKER = 9
        const val GRANDE = 10
    }
}