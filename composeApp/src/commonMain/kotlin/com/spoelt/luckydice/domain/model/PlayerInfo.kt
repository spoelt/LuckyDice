package com.spoelt.luckydice.domain.model

/**
 * Represents the available information of a player in a dice poker game.
 *
 * @property id The unique ID of the player.
 * @property name The player's name.
 * @property columns A list of columns associated with the player, where each column tracks the
 *                   points the player has achieved in that specific column. The list represents
 *                   the player's performance across multiple columns in the game.
 */
data class PlayerInfo(
    val id: Long = 0,
    val name: String,
    val columns: List<PlayerColumn>
)