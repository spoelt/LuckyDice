package com.spoelt.luckydice.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spoelt.luckydice.presentation.home.HomeScreen
import com.spoelt.luckydice.presentation.home.HomeViewModel
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes.SelectNumberOfPlayers.getGameType
import com.spoelt.luckydice.presentation.selectgameoptions.SelectGameOptionsViewModel
import com.spoelt.luckydice.presentation.selectgameoptions.enterplayernames.EnterPlayerNames
import com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofplayers.SelectNumberOfPlayers
import com.spoelt.luckydice.presentation.theme.LuckyDiceTheme
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
) {
    val navController = rememberNavController()
    val gameOptionsViewModel = koinViewModel<SelectGameOptionsViewModel>()

    LuckyDiceTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        KoinContext {
            NavHost(
                navController = navController,
                startDestination = NavigationRoutes.Home.route
            ) {
                composable(route = NavigationRoutes.Home.route) {
                    val viewModel = koinViewModel<HomeViewModel>()
                    val gameTypes by viewModel.gameTypes.collectAsStateWithLifecycle()

                    HomeScreen(
                        gameTypes = gameTypes,
                        onGameTypeSelected = { type ->
                            val route = NavigationRoutes.SelectNumberOfPlayers.createRoute(type)
                            navController.navigate(route)
                        }
                    )
                }

                composable(
                    route = NavigationRoutes.SelectNumberOfPlayers.route,
                    arguments = NavigationRoutes.SelectNumberOfPlayers.createArgumentsList()
                ) { backStackEntry ->
                    backStackEntry.arguments?.getGameType()?.let { type -> // TODO: update type specific setup when more games available
                        val gameOptions by gameOptionsViewModel.gameOptions.collectAsStateWithLifecycle()

                        SelectNumberOfPlayers(
                            numberOfPlayers = gameOptions.numberOfPlayers,
                            onSelectedNumberChange = gameOptionsViewModel::setNumberOfPlayers,
                            onNextClick = {
                                gameOptionsViewModel.initializePlayersMap()

                                navController.navigate(
                                    NavigationRoutes.EnterPlayerNames.route
                                )
                            },
                            onCloseClick = navController::popBackStack,
                        )
                    } ?: navController.popBackStack()
                }

                composable(route = NavigationRoutes.EnterPlayerNames.route) {
                    val gameOptions by gameOptionsViewModel.gameOptions.collectAsStateWithLifecycle()

                    EnterPlayerNames(
                        onNextClick = {},
                        onBackClick = navController::popBackStack,
                        onCloseClick = {
                            navController.popBackStack(
                                route = NavigationRoutes.Home.route,
                                inclusive = false
                            )
                        },
                        players = gameOptions.players,
                        onUpdatePlayerName = gameOptionsViewModel::updatePlayerName
                    )
                }
            }
        }
    }
}