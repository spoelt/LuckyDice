package com.spoelt.luckydice.domain.model

data class PlayerResult(
    val name: String,
    val finalPoints: Int,
    val ranking: PlayerRanking
)

enum class PlayerRanking {
    FIRST_PLACE, SECOND_PLACE, THIRD_PLACE, OTHER
}
