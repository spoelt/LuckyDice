package com.spoelt.luckydice.presentation.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerPoints

const val MAX_TWO_DIGIT_INPUT = 2
const val MAX_THREE_DIGIT_INPUT = 3

@Composable
fun PlayerPointsColumn(
    modifier: Modifier = Modifier,
    elementHeight: Dp = TextFieldDefaults.MinHeight,
    points: Map<Int, PlayerPoints>,
    onPointsChange: (Pair<Int, String>) -> Unit,
    isSelected: Boolean,
) {
    val sortedPoints = remember(points) {
        // use this hack because .toSortedMap() is only available on Android
        points.entries.sortedBy { it.key }.associate { it.toPair() }
    }
    val lastKey = remember(sortedPoints) {
        sortedPoints.entries.lastOrNull()?.key
    }
    val verticalArrangement = remember(isSelected) {
        if (isSelected) Arrangement.spacedBy(4.dp) else Arrangement.Center
    }

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        if (isSelected) {
            sortedPoints.forEach { (key, playerPoints) ->
                PointsInputField(
                    modifier = Modifier.fillMaxWidth(),
                    points = playerPoints.pointsValue,
                    onPointsChange = { value ->
                        onPointsChange(key to value)
                    },
                    maxChars = if (key <= PlayerColumn.POKER) {
                        MAX_TWO_DIGIT_INPUT
                    } else {
                        MAX_THREE_DIGIT_INPUT
                    },
                    isError = playerPoints.error
                )
            }
        } else {
            sortedPoints.forEach { (key, playerPoints) ->
                PointsDisplayField(
                    modifier = Modifier
                        .padding(bottom = if (lastKey == key) 0.dp else 6.dp)
                        .height(elementHeight)
                        .fillMaxWidth(),
                    points = playerPoints.pointsValue
                )
            }
        }
    }
}

@Composable
private fun PointsInputField(
    modifier: Modifier = Modifier,
    points: Int,
    isError: Boolean,
    onPointsChange: (String) -> Unit,
    maxChars: Int,
) {
    val focusManager = LocalFocusManager.current
    val displayedPoints = remember(points) {
        if (points == -1) "" else "$points"
    }

    TextField(
        modifier = modifier,
        value = displayedPoints,
        onValueChange = { value ->
            if (value.length <= maxChars) {
                onPointsChange(value)
            }
        },
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        isError = isError
    )
}

@Composable
private fun PointsDisplayField(
    modifier: Modifier = Modifier,
    points: Int,
) {
    val displayedPoints = remember(points) {
        if (points == -1) "" else "$points"
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayedPoints,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}