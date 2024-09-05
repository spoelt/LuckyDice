package com.spoelt.luckydice.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.spoelt.luckydice.presentation.util.LoggingUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        LoggingUtil.initialize()

        setContent {
            App(darkTheme = isSystemInDarkTheme())
        }
    }
}