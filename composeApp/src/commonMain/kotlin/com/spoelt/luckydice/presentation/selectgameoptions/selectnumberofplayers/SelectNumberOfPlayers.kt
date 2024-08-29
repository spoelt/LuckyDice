package com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofplayers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.selectgameoptions.components.GameOptionsScaffold
import com.spoelt.luckydice.presentation.selectgameoptions.components.NumberButtonRow
import luckydice.composeapp.generated.resources.Res
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

    GameOptionsScaffold(
        modifier = modifier,
        mainContent = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(Res.string.select_number_of_players_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NumberButtonRow(
                    range = 1..2,
                    selectedNumber = selectedNumber,
                    onClick = onSelectedNumberChange
                )
                NumberButtonRow(
                    range = 3..4,
                    selectedNumber = selectedNumber,
                    onClick = onSelectedNumberChange
                )
            }
        },
        bottomBarContent = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                onClick = {
                    onNextClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = stringResource(Res.string.next_action),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        isBackButtonVisible = false,
        onBackClick = {
            // do nothing - no back button available
        },
        onCloseClick = onCloseClick
    )
}

