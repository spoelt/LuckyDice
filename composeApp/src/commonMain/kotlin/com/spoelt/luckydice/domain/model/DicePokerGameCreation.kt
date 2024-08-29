package com.spoelt.luckydice.domain.model

/**
 * Represents the configuration options for a game of dice poker when creating the game.
 *
 * @property numberOfPlayers The number of players. Defaults to 2.
 * @property players A map of players participating in the game.
 *                   The key is the player's temporary unique identifier (an integer), and the value
 *                   is the name of the player.
 * @property numberOfColumns The number of columns the game should have. Defaults to 2.
 */
data class DicePokerGameCreation(
    val numberOfPlayers: Int = 2,
    val players: Map<Int, String>? = null,
    val numberOfColumns: Int = 2
)
