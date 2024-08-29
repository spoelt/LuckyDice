package com.spoelt.luckydice.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.theme.primaryGradient
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.dice
import luckydice.composeapp.generated.resources.home_dice_icon_content_desc
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CardWithCutOutImage(
    modifier: Modifier = Modifier,
    gradientBackground: Brush = primaryGradient,
    imageHeight: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    val imageTopPadding = remember(imageHeight) {
        imageHeight.value.div(2)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .padding(top = imageTopPadding.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(gradientBackground)
                .padding(top = imageTopPadding.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBackground)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }

        DiceImage(primaryGradient)
    }
}

@Composable
private fun DiceImage(gradientBackground: Brush) {
    Image(
        modifier = Modifier
            .size(100.dp)
            .border(
                border = BorderStroke(width = 10.dp, color = MaterialTheme.colorScheme.surface),
                shape = CircleShape
            )
            .padding(10.dp)
            .clip(CircleShape)
            .background(gradientBackground),
        painter = painterResource(Res.drawable.dice),
        contentDescription = stringResource(Res.string.home_dice_icon_content_desc),
        contentScale = ContentScale.Inside,
    )
}