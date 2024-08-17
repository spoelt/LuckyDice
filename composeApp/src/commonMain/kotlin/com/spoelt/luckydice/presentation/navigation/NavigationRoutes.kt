package com.spoelt.luckydice.presentation.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.spoelt.luckydice.domain.GameType

private const val HOME_ROUTE = "home"
private const val SELECT_NUMBER_OF_PLAYERS_BASE_ROUTE = "select_number_of_players"
private const val ENTER_PLAYER_NAMES_ROUTE = "enter_player_names"

sealed class NavigationRoutes(val route: String) {
    data object Home : NavigationRoutes(HOME_ROUTE)

    data object SelectNumberOfPlayers : NavigationRoutes("$SELECT_NUMBER_OF_PLAYERS_BASE_ROUTE/{gameType}") {
        private const val ARGUMENT_NAME = "gameType"

        fun createArgumentsList() = listOf(
            navArgument(ARGUMENT_NAME) { type = NavType.StringType }
        )

        fun createRoute(type: GameType): String {
            return "$SELECT_NUMBER_OF_PLAYERS_BASE_ROUTE/${type.value}"
        }

        fun Bundle?.getGameType(): GameType? {
            if (this == null) return null

            return this.getString(ARGUMENT_NAME)?.let { type ->
                GameType from type
            }
        }
    }

    data object EnterPlayerNames:NavigationRoutes(ENTER_PLAYER_NAMES_ROUTE)
}