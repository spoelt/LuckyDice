package com.spoelt.luckydice.data.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.spoelt.luckydice.data.local.dao.LuckyDiceDao
import com.spoelt.luckydice.data.model.DicePokerGameCreationEntity
import com.spoelt.luckydice.data.model.PlayerColumnEntity
import com.spoelt.luckydice.data.model.PlayerInfoEntity
import com.spoelt.luckydice.data.model.PlayerPointsEntity

@Database(
    entities = [
        DicePokerGameCreationEntity::class,
        PlayerInfoEntity::class,
        PlayerColumnEntity::class,
        PlayerPointsEntity::class
    ],
    version = 1
)
@ConstructedBy(LuckyDiceDatabaseConstructor::class)
abstract class LuckyDiceDatabase : RoomDatabase(), DB {
    abstract fun getDicePokerGameDao(): LuckyDiceDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// https://issuetracker.google.com/issues/348166275
interface DB {
    fun clearAllTables() {}
}