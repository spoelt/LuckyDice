package com.spoelt.luckydice.data.local.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getDatabaseBuilder(): LuckyDiceDatabase {
    val databaseFile = "${NSHomeDirectory()}/lucky_dice_database.db"
    return Room.databaseBuilder<LuckyDiceDatabase>(
        name = databaseFile,
        factory = { LuckyDiceDatabase::class.instantiateImpl() }
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}