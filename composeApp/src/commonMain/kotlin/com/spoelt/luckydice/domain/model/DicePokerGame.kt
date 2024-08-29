package com.spoelt.luckydice.domain.model

/**
 * Represents a game of dice poker.
 *
 * @property numberOfPlayers The number of players.
 * @property players A map of players participating in the game.
 *                   The key is the player's unique identifier and the value represents
 *                   information about the player (name and points made in the game).
 * @property numberOfColumns The number of columns the game of dice poker has.
 */
data class DicePokerGame(
    val id: Long = 0,
    val numberOfPlayers: Int,
    val players: List<PlayerInfo>,
    val numberOfColumns: Int
)
