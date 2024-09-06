package com.spoelt.luckydice.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.spoelt.luckydice.domain.model.LuckyDiceNavigationTarget
import com.spoelt.luckydice.presentation.game.Game
import com.spoelt.luckydice.presentation.game.GameViewModel
import com.spoelt.luckydice.presentation.home.HomeScreen
import com.spoelt.luckydice.presentation.home.HomeViewModel
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes.GameScreen.getGameId
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes.ResultsScreen.getGameIdForResults
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes.SelectNumberOfPlayers.getGameType
import com.spoelt.luckydice.presentation.results.Results
import com.spoelt.luckydice.presentation.selectgameoptions.SelectGameOptionsViewModel
import com.spoelt.luckydice.presentation.selectgameoptions.enterplayernames.EnterPlayerNames
import com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofcolumns.SelectNumberOfColumns
import com.spoelt.luckydice.presentation.selectgameoptions.selectnumberofplayers.SelectNumberOfPlayers
import com.spoelt.luckydice.presentation.theme.LuckyDiceTheme
import luckydice.composeapp.generated.resources.Res
import luckydice.composeapp.generated.resources.invalid_input
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App(darkTheme: Boolean) {
    val navController = rememberNavController()
    val gameOptionsViewModel = koinViewModel<SelectGameOptionsViewModel>()

    LaunchedEffect(Unit) {
        gameOptionsViewModel.navigateEvent.collect { target ->
            navController.customNavigate(target)
            target.popUpToRoute?.let {
                gameOptionsViewModel.reset()
            }
        }
    }

    LuckyDiceTheme(darkTheme = darkTheme) {
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
                                navController.navigate(NavigationRoutes.EnterPlayerNames.route)
                            },
                            onCloseClick = {
                                navController.popBackStack()
                                gameOptionsViewModel.reset()
                            },
                        )
                    } ?: navController.popBackStack()
                }

                composable(route = NavigationRoutes.EnterPlayerNames.route) {
                    val gameOptions by gameOptionsViewModel.gameOptions.collectAsStateWithLifecycle()
                    val gameType by gameOptionsViewModel.gameType.collectAsStateWithLifecycle()

                    EnterPlayerNames(
                        onNextClick = {
                            /*val route = if (gameType.isDicePoker()) {
                                NavigationRoutes.SelectNumberOfColumns.route
                            } else {
                                // TODO: create game in view model and then navigate
                            }*/
                            val route = NavigationRoutes.SelectNumberOfColumns.route
                            navController.navigate(route)
                        },
                        onBackClick = navController::popBackStack,
                        onCloseClick = {
                            navController.popBackStack(
                                route = NavigationRoutes.Home.route,
                                inclusive = false
                            )
                            gameOptionsViewModel.reset()
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
                            gameOptionsViewModel.reset()
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
                    getGameId(backStackEntry.arguments)?.let { id ->
                        val viewModel = koinViewModel<GameViewModel>()
                        val game by viewModel.game.collectAsStateWithLifecycle()
                        val selectedPlayerId by viewModel.selectedPlayerId.collectAsStateWithLifecycle()
                        val snackbarHostState = remember { SnackbarHostState() }
                        val errorMessage = stringResource(Res.string.invalid_input)
                        val lifecycleOwner = LocalLifecycleOwner.current

                        LaunchedEffect(Unit) {
                            viewModel.navigateEvent.collect { target ->
                                navController.customNavigate(target)
                            }
                        }

                        LaunchedEffect(Unit) {
                            viewModel.snackbar.collect {
                                snackbarHostState.showSnackbar(
                                    message = errorMessage
                                )
                            }
                        }

                        DisposableEffect(lifecycleOwner) {
                            val lifecycle = lifecycleOwner.lifecycle
                            val observer = LifecycleEventObserver { _, event ->
                                when (event) {
                                    Lifecycle.Event.ON_START -> {
                                        viewModel.startSaveGameJob()
                                        viewModel.getGame(id)
                                    }

                                    Lifecycle.Event.ON_STOP -> viewModel.saveGameOnStop()

                                    else -> Unit
                                }
                            }

                            lifecycle.addObserver(observer)

                            onDispose {
                                lifecycle.removeObserver(observer)
                            }
                        }

                        game?.let { g ->
                            Game(
                                modifier = Modifier.fillMaxSize(),
                                playerInfos = g.players,
                                selectedPlayerId = selectedPlayerId,
                                onSelectedPlayerClick = viewModel::updateSelectedPlayer,
                                onPointsChange = viewModel::updatePoints,
                                onEndGameClick = viewModel::finishGame,
                                snackbarHostState = snackbarHostState,
                            )
                        }
                    } ?: navController.popBackStack()
                }

                composable(
                    route = NavigationRoutes.ResultsScreen.route,
                    arguments = NavigationRoutes.ResultsScreen.createArgumentsList(),
                ) { backStackEntry ->
                    getGameIdForResults(backStackEntry.arguments)?.let { id ->
                        Results(
                            modifier = Modifier.fillMaxSize(),
                            gameId = id,
                            onGoHome = navController::popBackStack
                        )
                    } ?: navController.popBackStack()
                }
            }
        }
    }
}

fun NavController.customNavigate(target: LuckyDiceNavigationTarget) {
    target.popUpToRoute?.let { popUpTo ->
        navigate(
            target.route,
            navOptions {
                popUpTo(popUpTo) {
                    inclusive = target.inclusive
                }
            }
        )
    } ?: navigate(target.route)
}