package com.spoelt.luckydice.presentation.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spoelt.luckydice.domain.model.PlayerRanking
import com.spoelt.luckydice.domain.model.PlayerResult
import com.spoelt.luckydice.presentation.customNavigate
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.first_place_content_desc
import luckydice.composeapp.generated.resources.go_home_action
import luckydice.composeapp.generated.resources.player_points
import luckydice.composeapp.generated.resources.second_place
import luckydice.composeapp.generated.resources.second_place_content_desc
import luckydice.composeapp.generated.resources.third_place
import luckydice.composeapp.generated.resources.third_place_content_desc
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun Results(
    modifier: Modifier = Modifier,
    gameId: Long,
    viewModel: ResultsViewModel = koinViewModel(),
    onGoHome: () -> Unit,
) {
    val players by viewModel.players.collectAsStateWithLifecycle()
    val winners = remember(players) {
        players.filter { it.ranking == PlayerRanking.FIRST_PLACE }
    }
    val otherPlayers = remember(players) {
        players.filterNot { it.ranking == PlayerRanking.FIRST_PLACE }
    }

    LaunchedEffect(gameId) {
        viewModel.getPlayerPoints(gameId)
    }

    LaunchedEffect(Unit) {
        viewModel.goBackEvent.collect {
            onGoHome()
        }
    }

    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FirstPlace(winners)

                otherPlayers.forEach { player ->
                    OtherPlayers(player)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 6.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                onClick = onGoHome,
            ) {
                Text(
                    text = stringResource(Res.string.go_home_action),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun OtherPlayers(player: PlayerResult) {
    val imageResource = remember(player) {
        when (player.ranking) {
            PlayerRanking.SECOND_PLACE -> Res.drawable.second_place
            PlayerRanking.THIRD_PLACE -> Res.drawable.third_place
            else -> null
        }
    }
    val contentDesc = remember(player) {
        when (player.ranking) {
            PlayerRanking.SECOND_PLACE -> Res.string.second_place_content_desc
            PlayerRanking.THIRD_PLACE -> Res.string.third_place_content_desc
            else -> null
        }
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageResource?.let { imageRes ->
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(imageRes),
                contentDescription = contentDesc?.let { desc ->
                    stringResource(desc)
                },
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                text = player.name,
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = pluralStringResource(
                    Res.plurals.player_points,
                    player.finalPoints,
                    player.finalPoints
                ),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun FirstPlace(players: List<PlayerResult>) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/first_place.json").decodeToString()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp)
            .padding(top = 6.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                iterations = Compottie.IterateForever
            ),
            contentDescription = stringResource(Res.string.first_place_content_desc)
        )

        Column(
            modifier = Modifier.offset(y = (-12).dp)
        ) {
            players.forEachIndexed { index, player ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp),
                    text = player.name,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = pluralStringResource(
                        Res.plurals.player_points,
                        player.finalPoints,
                        player.finalPoints
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                if (index != players.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
