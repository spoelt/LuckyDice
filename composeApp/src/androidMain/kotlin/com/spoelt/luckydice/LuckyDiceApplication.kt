package com.spoelt.luckydice

import android.app.Application
import com.spoelt.luckydice.di.initKoin

class LuckyDiceApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}