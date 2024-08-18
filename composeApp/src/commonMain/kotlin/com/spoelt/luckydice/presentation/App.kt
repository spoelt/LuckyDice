package com.spoelt.luckydice.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spoelt.luckydice.domain.model.isDicePoker
import com.spoelt.luckydice.presentation.home.HomeScreen
import com.spoelt.luckydice.presentation.home.HomeViewModel
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes.GameScreen.getGameId
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes.SelectNumberOfPlayers.getGameType
import com.spoelt.luckydice.presentation.selectgameoptions.SelectGameOptionsViewModel
import com.spoelt.luckydice.presentation.selectgameoptions.enterplayernames.EnterPlayerNames
import com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofcolumns.SelectNumberOfColumns
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

    LaunchedEffect(Unit) {
        gameOptionsViewModel.navigate.collect { route ->
            navController.navigate(route)
        }
    }

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
                    backStackEntry.arguments?.getGameType()?.let { type ->
                        // TODO: update type specific setup when more games available
                            LaunchedEffect(Unit) {
                                gameOptionsViewModel.setGameType(type)
                            }

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
                    val gameType by gameOptionsViewModel.gameType.collectAsStateWithLifecycle()

                    EnterPlayerNames(
                        onNextClick = {
                            val route = if (gameType.isDicePoker()) {
                                NavigationRoutes.SelectNumberOfColumns.route
                            } else {
                                NavigationRoutes.GameScreen.route
                            }
                            navController.navigate(route)
                        },
                        onBackClick = navController::popBackStack,
                        onCloseClick = {
                            navController.popBackStack(
                                route = NavigationRoutes.Home.route,
                                inclusive = false
                            )
                        },
                        players = gameOptions.players,
                        onUpdatePlayerName = gameOptionsViewModel::updatePlayerName,
                        type = gameType
                    )
                }

                composable(route = NavigationRoutes.SelectNumberOfColumns.route) {
                    val gameOptions by gameOptionsViewModel.gameOptions.collectAsStateWithLifecycle()

                    SelectNumberOfColumns(
                        selectedNumber = gameOptions.numberOfColumns,
                        onCloseClick = {
                            navController.popBackStack(
                                route = NavigationRoutes.Home.route,
                                inclusive = false
                            )
                        },
                        onBackClick = navController::popBackStack,
                        onSelectedNumberChange = gameOptionsViewModel::updateNumberOfColumns,
                        onNextClick = gameOptionsViewModel::createGame
                    )
                }

                composable(
                    route = NavigationRoutes.GameScreen.route,
                    arguments = NavigationRoutes.GameScreen.createArgumentsList(),
                ) { backStackEntry ->
                    backStackEntry.arguments?.getGameId()?.let { id ->
                        /*LaunchedEffect(Unit) {
                            gameOptionsViewModel.setGameType(type)
                        }*/

                        Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                            Text(
                                text = "$id"
                            )
                        }
                    } ?: navController.popBackStack()
                }
            }
        }
    }
}