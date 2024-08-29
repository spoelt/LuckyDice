package com.spoelt.luckydice

import androidx.compose.ui.window.ComposeUIViewController
import com.spoelt.luckydice.di.initKoin
import com.spoelt.luckydice.presentation.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App(darkTheme = false)
}
