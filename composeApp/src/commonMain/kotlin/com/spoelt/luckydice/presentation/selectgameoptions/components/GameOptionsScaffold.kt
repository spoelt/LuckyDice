package com.spoelt.luckydice.presentation.selectgameoptions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.back_content_desc
import luckydice.composeapp.generated.resources.close_content_desc
import luckydice.composeapp.generated.resources.ic_back
import luckydice.composeapp.generated.resources.ic_close

@Composable
fun GameOptionsScaffold(
    modifier: Modifier = Modifier,
    mainContent: @Composable () -> Unit,
    bottomBarContent: @Composable () -> Unit,
    isBackButtonVisible: Boolean,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            bottomBarContent()
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                mainContent()

                Spacer(modifier = Modifier.weight(2f))
            }

            if (isBackButtonVisible) {
                LuckyDiceIconButton(
                    buttonModifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                        .align(Alignment.TopStart),
                    iconModifier = Modifier.size(24.dp).offset(x = (-2).dp),
                    onClick = onBackClick,
                    contentDescriptionId = Res.string.back_content_desc,
                    iconId = Res.drawable.ic_back
                )
            }

            LuckyDiceIconButton(
                buttonModifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .align(Alignment.TopEnd),
                iconModifier = Modifier.size(28.dp),
                onClick = onCloseClick,
                contentDescriptionId = Res.string.close_content_desc,
                iconId = Res.drawable.ic_close
            )
        }
    }
}