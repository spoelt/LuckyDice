package com.spoelt.luckydice.domain

/**
 * Represents the configuration options for a game of dice poker.
 *
 * @property numberOfPlayers The number of players. Defaults to 2.
 * @property players A map of players participating in the game.
 *                   The key is the player's unique identifier (an integer), and the value is the
 *                   player's name.
 * @property numberOfColumns The number of columns the game should have. Defaults to 2.
 */
data class DicePokerGameOptions(
    val numberOfPlayers: Int = 2,
    val players: Map<Int, String>? = null,
    val numberOfColumns: Int = 2
)