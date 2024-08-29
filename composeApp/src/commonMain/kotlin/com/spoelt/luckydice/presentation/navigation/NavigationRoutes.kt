package com.spoelt.luckydice.presentation.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.spoelt.luckydice.domain.model.GameType

private const val HOME_ROUTE = "home"
private const val SELECT_NUMBER_OF_PLAYERS_BASE_ROUTE = "select_number_of_players"
private const val ENTER_PLAYER_NAMES_ROUTE = "enter_player_names"
private const val SELECT_NUMBER_OF_COLUMNS_ROUTE = "select_number_of_columns"
private const val GAME_SCREEN_BASE_ROUTE = "game_screen"
private const val RESULTS_SCREEN_BASE_ROUTE = "results_screen"

sealed class NavigationRoutes(val route: String) {
    data object Home : NavigationRoutes(HOME_ROUTE)

    data object SelectNumberOfPlayers :
        NavigationRoutes("$SELECT_NUMBER_OF_PLAYERS_BASE_ROUTE/{gameType}") {
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

    data object EnterPlayerNames : NavigationRoutes(ENTER_PLAYER_NAMES_ROUTE)

    data object SelectNumberOfColumns : NavigationRoutes(SELECT_NUMBER_OF_COLUMNS_ROUTE)

    data object GameScreen : NavigationRoutes("$GAME_SCREEN_BASE_ROUTE/{gameId}") {
        private const val ARGUMENT_GAME_ID = "gameId"

        fun createArgumentsList() = listOf(
            navArgument(ARGUMENT_GAME_ID) { type = NavType.LongType }
        )

        fun createRoute(gameId: Long): String {
            return "$GAME_SCREEN_BASE_ROUTE/$gameId"
        }

        fun getGameId(bundle: Bundle?): Long? {
            if (bundle == null) return null

            return bundle.getLong(ARGUMENT_GAME_ID)
        }
    }

    data object ResultsScreen : NavigationRoutes("$RESULTS_SCREEN_BASE_ROUTE/{gameId}") {
        private const val ARGUMENT_GAME_ID = "gameId"

        fun createArgumentsList() = listOf(
            navArgument(ARGUMENT_GAME_ID) { type = NavType.LongType }
        )

        fun createRoute(gameId: Long): String {
            return "$RESULTS_SCREEN_BASE_ROUTE/$gameId"
        }

        fun getGameIdForResults(bundle: Bundle?): Long? {
            if (bundle == null) return null

            return bundle.getLong(ARGUMENT_GAME_ID)
        }
    }
}