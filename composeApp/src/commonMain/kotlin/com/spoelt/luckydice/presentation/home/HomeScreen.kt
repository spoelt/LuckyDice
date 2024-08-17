package com.spoelt.luckydice.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.GameType
import com.spoelt.luckydice.presentation.home.components.CardWithCutOutImage
import com.spoelt.luckydice.presentation.home.components.GameOptions
import com.spoelt.luckydice.presentation.home.components.Header

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    gameTypes: List<GameType>,
    onGameTypeSelected: (GameType) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .safeDrawingPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                CardWithCutOutImage {
                    Header()

                    Spacer(modifier = Modifier.height(16.dp))

                    GameOptions(
                        gameTypes = gameTypes,
                        onGameTypeSelected = onGameTypeSelected
                    )
                }
            }
        }
    }
}