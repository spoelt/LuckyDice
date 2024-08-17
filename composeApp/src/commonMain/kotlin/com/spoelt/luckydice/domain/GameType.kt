package com.spoelt.luckydice.domain

enum class GameType(val value: String) {
    DICE_POKER("dice_poker"), YAHTZEE("yahtzee"), FARKLES("farkles");

    companion object : EnumFinder<String, GameType>(entries.associateBy { it.value })
}

fun GameType?.isDicePoker() = this?.let { it == GameType.DICE_POKER } ?: false
