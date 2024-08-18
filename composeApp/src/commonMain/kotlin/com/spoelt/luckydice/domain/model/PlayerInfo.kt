package com.spoelt.luckydice.domain.model

/**
 * Represents the available information of a player.
 *
 * @property id The ID of the player saved in the data base.
 * @property name The player's name.
 * @property points The points the player has achieved.
 */
data class PlayerInfo(
    val id: Long = 0,
    val name: String,
    val points: Int
)