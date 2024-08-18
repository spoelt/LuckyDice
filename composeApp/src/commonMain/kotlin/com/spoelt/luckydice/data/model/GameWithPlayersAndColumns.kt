package com.spoelt.luckydice.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerInfo

data class GameWithPlayersAndColumns(
    @Embedded val game: DicePokerGameEntity,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "game_id",
        entity = PlayerInfoEntity::class
    )
    val playersWithColumns: List<PlayerWithColumns>
)

data class PlayerWithColumns(
    @Embedded val player: PlayerInfoEntity,
    @Relation(
        parentColumn = "player_id",
        entityColumn = "player_id"
    )
    val columns: List<PlayerColumnEntity>
)

fun GameWithPlayersAndColumns.toDicePokerGame(): DicePokerGame {
    val players = mutableMapOf<Long, PlayerInfo>()
    this.playersWithColumns.forEach { playerWithColumns ->
        players[playerWithColumns.player.playerId] = PlayerInfo(
            id = playerWithColumns.player.playerId,
            name = playerWithColumns.player.name,
            columns = playerWithColumns.columns.toPlayerColumnList()
        )
    }
    return DicePokerGame(
        numberOfPlayers = this.game.numberOfPlayers,
        numberOfColumns = this.game.numberOfColumns,
        players = players
    )
}