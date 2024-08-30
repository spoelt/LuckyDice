package com.spoelt.luckydice.domain.model

data class PlayerPoints(
    val pointId: Long,
    val pointsValue: Int,
    val error: Boolean = false
)
