package com.spoelt.luckydice.domain.model

/**
 * Data class representing the points a player has in a specific column of a dice poker game.
 *
 * @property columnId The unique ID for the player's column entry.
 * @property columnNumber The number of the column in which the points are recorded.
 *                        Column numbers typically start from 1 and go up to 3.
 * @property points The map of the points the player has earned in this specific column.
 */
data class PlayerColumn(
    val columnId: Long,
    val columnNumber: Int,
    val points: Map<Int, PlayerPoints>,
) {
    companion object {
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SIX = 6
        const val STREET = 7
        const val FULL_HOUSE = 8
        const val POKER = 9
        const val GRANDE = 10
    }
}