package com.spoelt.luckydice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.DicePokerGameCreation

@Entity(tableName = "dice_poker_games")
data class DicePokerGameCreationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    val gameId: Long = 0,

    @ColumnInfo(name = "number_of_players")
    val numberOfPlayers: Int,

    @ColumnInfo(name = "number_of_columns")
    val numberOfColumns: Int
)

