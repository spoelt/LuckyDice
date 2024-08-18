package com.spoelt.luckydice.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun getDatabaseBuilder(context: Context): LuckyDiceDatabase {
    val databasePath = context.getDatabasePath("lucky_dice_database.db")
    return Room.databaseBuilder<LuckyDiceDatabase>(context, databasePath.absolutePath)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}