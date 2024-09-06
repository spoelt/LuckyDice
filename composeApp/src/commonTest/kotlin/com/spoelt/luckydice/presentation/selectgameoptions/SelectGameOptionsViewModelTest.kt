package com.spoelt.luckydice.presentation.selectgameoptions

import app.cash.turbine.test
import com.spoelt.luckydice.domain.model.DicePokerGameCreation
import com.spoelt.luckydice.domain.model.GameType
import com.spoelt.luckydice.domain.model.LuckyDiceNavigationTarget
import com.spoelt.luckydice.domain.repository.GameRepository
import com.spoelt.luckydice.presentation.navigation.NavigationRoutes
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class SelectGameOptionsViewModelTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private val gameRepository: GameRepository = mock<GameRepository>()
    private lateinit var viewModel: SelectGameOptionsViewModel

    private val testModule = module {
        single { SelectGameOptionsViewModel(gameRepository) }
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(testModule)
        }
        viewModel = get()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `setGameType updates gameType state`() = runTest {
        viewModel.apply {
            setGameType(GameType.DICE_POKER)

            gameType.test {
                assertEquals(GameType.DICE_POKER, awaitItem())
            }
        }
    }

    @Test
    fun `setNumberOfPlayers updates numberOfPlayers in gameOptions`() = runTest {
        viewModel.apply {
            setNumberOfPlayers(4)

            gameOptions.test {
                assertEquals(4, awaitItem().numberOfPlayers)
            }
        }
    }

    @Test
    fun `initializePlayersMap creates correct map of players`() = runTest {
        viewModel.apply {
            setNumberOfPlayers(3)
            initializePlayersMap()
            val expectedPlayers = mapOf(0 to "", 1 to "", 2 to "")

            gameOptions.test {
                assertEquals(expectedPlayers, awaitItem().players)
            }
        }
    }

    @Test
    fun `updatePlayerName correctly updates player name`() = runTest {
        viewModel.apply {
            setNumberOfPlayers(2)
            initializePlayersMap()
            updatePlayerName(0, "Alice")

            gameOptions.test {
                val emission = awaitItem()
                assertEquals("Alice", emission.players?.get(0))
                assertEquals("", emission.players?.get(1))
            }
        }
    }

    @Test
    fun `updateNumberOfColumns updates numberOfColumns in gameOptions`() = runTest {
        viewModel.apply {
            updateNumberOfColumns(3)

            gameOptions.test {
                assertEquals(3, awaitItem().numberOfColumns)
            }
        }
    }

    @Test
    fun `createGame trims player names and calls repository`() = runTest {
        viewModel.apply {
            setNumberOfPlayers(2)
            initializePlayersMap()
            updatePlayerName(0, "  Alice  ")
            updatePlayerName(1, "Bob  ")
            updateNumberOfColumns(3)
        }

        val expectedGameId = 123L
        everySuspend { gameRepository.createDicePokerGame(any()) } returns expectedGameId

        viewModel.navigateEvent.test {
            viewModel.createGame()

            val navigationEvent = awaitItem()
            assertEquals(
                LuckyDiceNavigationTarget(
                    route = NavigationRoutes.GameScreen.createRoute(expectedGameId),
                    popUpToRoute = NavigationRoutes.Home.route
                ),
                navigationEvent
            )

            advanceUntilIdle()

            verifySuspend(mode = VerifyMode.exactly(1)) {
                gameRepository.createDicePokerGame(
                    DicePokerGameCreation(
                        numberOfPlayers = 2,
                        players = mapOf(0 to "Alice", 1 to "Bob"),
                        numberOfColumns = 3
                    )
                )
            }

            expectNoEvents()
        }
    }

    @Test
    fun `reset clears gameOptions and gameType`() = runTest {
        viewModel.apply {
            setGameType(GameType.DICE_POKER)
            setNumberOfPlayers(4)
            reset()

            gameOptions.test {
                assertEquals(DicePokerGameCreation(), awaitItem())
            }

            gameType.test {
                assertNull(awaitItem())
            }
        }
    }
}