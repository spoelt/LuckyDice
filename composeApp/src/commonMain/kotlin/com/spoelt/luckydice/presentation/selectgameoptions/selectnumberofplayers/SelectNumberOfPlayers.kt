package com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofplayers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import luckydice.composeapp.generated.resources.close_content_desc
import luckydice.composeapp.generated.resources.ic_close
import luckydice.composeapp.generated.resources.next_action
import luckydice.composeapp.generated.resources.select_number_of_players_title
import org.jetbrains.compose.resources.stringResource

const val DEFAULT_NUM_OF_PLAYERS = 2

@Composable
fun SelectNumberOfPlayers(
    modifier: Modifier = Modifier,
    numberOfPlayers: Int?,
    onSelectedNumberChange: (Int) -> Unit,
    onNextClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    val selectedNumber = remember(numberOfPlayers) {
        numberOfPlayers ?: DEFAULT_NUM_OF_PLAYERS
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                onClick = {
                    onNextClick()
                }
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
                    text = stringResource(Res.string.select_number_of_players_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PlayerButtonRow(
                        range = 1..2,
                        selectedNumber = selectedNumber,
                        onClick = onSelectedNumberChange
                    )
                    PlayerButtonRow(
                        range = 3..4,
                        selectedNumber = selectedNumber,
                        onClick = onSelectedNumberChange
                    )
                }

                Spacer(modifier = Modifier.weight(2f))
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

