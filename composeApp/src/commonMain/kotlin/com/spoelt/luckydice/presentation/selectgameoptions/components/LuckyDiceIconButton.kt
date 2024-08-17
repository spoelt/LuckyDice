package com.spoelt.luckydice.presentation.selectgameoptions.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LuckyDiceIconButton(
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentDescriptionId: StringResource,
    iconId: DrawableResource
) {
    IconButton(
        modifier = buttonModifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            modifier = iconModifier,
            painter = painterResource(iconId),
            contentDescription = stringResource(contentDescriptionId),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}