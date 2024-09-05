package com.spoelt.luckydice.presentation.util

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object LoggingUtil {
    fun initialize() {
        Napier.base(DebugAntilog())
    }
}