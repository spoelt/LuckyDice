package com.spoelt.luckydice.presentation.selectgameoptions.enterplayernames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.GameType
import com.spoelt.luckydice.domain.isDicePoker
import com.spoelt.luckydice.presentation.selectgameoptions.components.GameOptionsScaffold
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.enter_player_names
import luckydice.composeapp.generated.resources.next_action
import luckydice.composeapp.generated.resources.player_name
import luckydice.composeapp.generated.resources.start_game
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnterPlayerNames(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit,
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
    players: Map<Int, String>?,
    onUpdatePlayerName: (Int, String) -> Unit,
    type: GameType?
) {
    val isBottomBarButtonEnabled = remember(players) {
        players?.values?.none { it.isEmpty() } ?: false
    }
    val bottomBarStringResId = remember(type) {
        if (type.isDicePoker()) {
            Res.string.next_action
        } else {
            Res.string.start_game
        }
    }

    GameOptionsScaffold(
        modifier = modifier,
        mainContent = {
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
                                text = stringResource(Res.string.player_name, "${key + 1}"),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        singleLine = true
                    )
                }
            }
        },
        bottomBarContent = {
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
                    text = stringResource(bottomBarStringResId),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        isBackButtonVisible = true,
        onBackClick = onBackClick,
        onCloseClick = onCloseClick
    )
}