package com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofcolumns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.selectgameoptions.components.GameOptionsScaffold
import com.spoelt.luckydice.presentation.selectgameoptions.components.NumberButton
import com.spoelt.luckydice.presentation.selectgameoptions.components.NumberButtonRow
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.select_number_of_columns_title
import luckydice.composeapp.generated.resources.start_game
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectNumberOfColumns(
    modifier: Modifier = Modifier,
    selectedNumber: Int,
    onSelectedNumberChange: (Int) -> Unit,
    onNextClick: () -> Unit,
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    GameOptionsScaffold(
        modifier = modifier,
        mainContent = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(Res.string.select_number_of_columns_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumberButtonRow(
                    range = 1..2,
                    selectedNumber = selectedNumber,
                    onClick = onSelectedNumberChange
                )
                NumberButton(
                    number = 3,
                    isSelected = 3 == selectedNumber,
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
                }
            ) {
                Text(
                    text = stringResource(Res.string.start_game),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        isBackButtonVisible = true,
        onBackClick = onBackClick,
        onCloseClick = onCloseClick
    )
}