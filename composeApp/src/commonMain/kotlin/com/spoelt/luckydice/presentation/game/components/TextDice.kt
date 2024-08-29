package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.theme.primaryGradient
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.full_house_abbreviation
import luckydice.composeapp.generated.resources.grande_abbreviation
import luckydice.composeapp.generated.resources.poker_abbreviation
import luckydice.composeapp.generated.resources.street_abbreviation
import org.jetbrains.compose.resources.stringResource

@Composable
fun TextDice(type: TextDiceType) {
    val textResId = remember(type) {
        when (type) {
            TextDiceType.STREET -> Res.string.street_abbreviation
            TextDiceType.FULL_HOUSE -> Res.string.full_house_abbreviation
            TextDiceType.POKER -> Res.string.poker_abbreviation
            TextDiceType.GRANDE -> Res.string.grande_abbreviation
        }
    }

    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(primaryGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textResId),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

enum class TextDiceType {
    STREET, FULL_HOUSE, POKER, GRANDE
}