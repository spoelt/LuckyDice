package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.presentation.game.components.NumberedDiceType.*
import com.spoelt.luckydice.presentation.theme.primaryGradient

@Composable
fun NumberedDice(type: NumberedDiceType) {
    val hasCenterDice = remember(type) {
        type in listOf(ONE, THREE, FIVE)
    }
    val hasTopLeftDice = remember(type) {
        type in listOf(FOUR, FIVE, SIX)
    }
    val hasTopRightDice = remember(type) {
        type in listOf(TWO, THREE, FOUR, FIVE, SIX)
    }
    val hasBottomLeftDice = remember(type) {
        type in listOf(TWO, THREE, FOUR, FIVE, SIX)
    }
    val hasBottomRightDice = remember(type) {
        type in listOf(FOUR, FIVE, SIX)
    }
    val hasMiddleDice = remember {
        type == SIX
    }

    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(primaryGradient)
    ) {
        if (hasCenterDice) {
            DicePoint(modifier = Modifier.align(Alignment.Center))
        }

        if (hasTopLeftDice) {
            DicePoint(
                modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.TopStart)
            )
        }

        if (hasTopRightDice) {
            DicePoint(
                modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.TopEnd)
            )
        }

        if (hasBottomLeftDice) {
            DicePoint(
                modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.BottomStart)
            )
        }

        if (hasBottomRightDice) {
            DicePoint(
                modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.BottomEnd)
            )
        }

        if (hasMiddleDice) {
            MiddleDice(
                modifier = Modifier
                    .rotate(90f)
                    .matchParentSize()
            )
        }
    }
}

@Composable
private fun MiddleDice(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        DicePoint(
            modifier = Modifier
                .padding(3.dp)
                .align(Alignment.TopCenter)
        )
        DicePoint(
            modifier = Modifier
                .padding(3.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun DicePoint(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
    )
}

enum class NumberedDiceType {
    ONE, TWO, THREE, FOUR, FIVE, SIX,
}