package com.spoelt.luckydice.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spoelt.luckydice.domain.GameType
import com.spoelt.luckydice.presentation.home.HomeScreen
import com.spoelt.luckydice.presentation.theme.LuckyDiceTheme
import moe.tlaster.precompose.PreComposeApp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
) {
    PreComposeApp {
        LuckyDiceTheme(
            darkTheme = darkTheme,
            dynamicColor = dynamicColor
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                HomeScreen(
                    gameTypes = listOf(GameType.DICE_POKER, GameType.YAHTZEE, GameType.FARKLES),
                    onGameTypeSelected = {
                        // navigate
                    }
                )
            }
        }
    }
}