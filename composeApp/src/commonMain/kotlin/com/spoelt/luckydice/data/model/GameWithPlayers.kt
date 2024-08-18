package com.spoelt.luckydice.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerInfo

data class GameWithPlayers(
    @Embedded val game: DicePokerGameEntity,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "game_id"
    )
    val players: List<PlayerInfoEntity>
)

fun GameWithPlayers.toDicePokerGame(): DicePokerGame {
    val players = mutableMapOf<Long, PlayerInfo>()
    this.players.forEach { player ->
        players[player.playerId] = player.toPlayerInfo()
    }
    return DicePokerGame(
        numberOfPlayers = this.game.numberOfPlayers,
        numberOfColumns = this.game.numberOfColumns,
        players = players
    )
}