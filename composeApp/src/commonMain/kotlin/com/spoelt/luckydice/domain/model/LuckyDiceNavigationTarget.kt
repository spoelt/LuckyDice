package com.spoelt.luckydice.domain.model

data class LuckyDiceNavigationTarget(
    val route: String,
    val popUpToRoute: String? = null,
    val inclusive: Boolean = false
)
