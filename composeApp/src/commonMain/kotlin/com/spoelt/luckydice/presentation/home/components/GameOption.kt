package com.spoelt.luckydice.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.GameType
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.dice_background
import luckydice.composeapp.generated.resources.game_option_background_content_desc
import luckydice.composeapp.generated.resources.game_option_dice_poker
import luckydice.composeapp.generated.resources.game_option_yahtzee
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

const val CARD_SIZE = 150

@Composable
fun GameOption(
    modifier: Modifier = Modifier,
    type: GameType,
    onGameTypeSelected: (GameType) -> Unit
) {
    val textResId = remember(type) {
        when (type) {
            GameType.DICE_POKER -> Res.string.game_option_dice_poker
            GameType.YAHTZEE -> Res.string.game_option_yahtzee
        }
    }

    Card(
        modifier = modifier
            .size(CARD_SIZE.dp)
            .clickable {
                // TODO: remove once more games are available
                if (type == GameType.DICE_POKER) onGameTypeSelected(type)
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.size(CARD_SIZE.dp),
                painter = painterResource(resource = Res.drawable.dice_background),
                contentDescription = stringResource(Res.string.game_option_background_content_desc),
                contentScale = ContentScale.Crop
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 10.dp
                    ),
                    text = stringResource(textResId),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}