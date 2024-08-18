package com.spoelt.luckydice.di

import com.spoelt.luckydice.data.local.db.LuckyDiceDatabase
import com.spoelt.luckydice.data.local.db.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<LuckyDiceDatabase> { getDatabaseBuilder() }
}