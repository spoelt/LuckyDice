package com.spoelt.luckydice.presentation.selectgameoptions.enterplayernames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.selectgameoptions.components.LuckyDiceIconButton
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.back_content_desc
import luckydice.composeapp.generated.resources.close_content_desc
import luckydice.composeapp.generated.resources.enter_player_names
import luckydice.composeapp.generated.resources.ic_back
import luckydice.composeapp.generated.resources.ic_close
import luckydice.composeapp.generated.resources.next_action
import luckydice.composeapp.generated.resources.player_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnterPlayerNames(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit,
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
    players: Map<Int, String>?,
    onUpdatePlayerName: (Int, String) -> Unit
) {
    val isBottomBarButtonEnabled = remember(players) {
        players?.values?.none { it.isEmpty() } ?: false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                onClick = {
                    onNextClick()
                },
                enabled = isBottomBarButtonEnabled
            ) {
                Text(
                    text = stringResource(Res.string.next_action),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(Res.string.enter_player_names),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    players?.keys?.forEach { key ->
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            value = players[key].orEmpty(),
                            onValueChange = { name ->
                                onUpdatePlayerName(key, name)
                            },
                            label = {
                                Text(
                                    text = stringResource(Res.string.player_name, "${key+1}"),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(2f))
            }

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