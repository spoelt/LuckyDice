package com.spoelt.luckydice

import android.app.Application
import com.spoelt.luckydice.di.initKoin
import org.koin.android.ext.koin.androidContext

class LuckyDiceApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@LuckyDiceApplication)
        }
    }
}