package com.spoelt.luckydice.presentation.selectgameoptions.enterplayernames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.GameType
import com.spoelt.luckydice.domain.model.isDicePoker
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
    val focusRequesters = remember { List(players?.size ?: 1) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequesters.firstOrNull()?.requestFocus()
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
                players?.keys?.forEachIndexed { index, key ->
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f)
                            .focusRequester(focusRequesters[index]),
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
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = if (index < players.size - 1) {
                                ImeAction.Next
                            } else {
                                ImeAction.Done
                            }
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (index < players.size - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                } else {
                                    focusManager.clearFocus()
                                }
                            },
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
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
                    focusManager.clearFocus()
                    onNextClick()
                },
                enabled = isBottomBarButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
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