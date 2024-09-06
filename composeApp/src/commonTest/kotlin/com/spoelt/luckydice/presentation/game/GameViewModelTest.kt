package com.spoelt.luckydice.presentation.game

import app.cash.turbine.test
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerInfo
import com.spoelt.luckydice.domain.model.PlayerPoints
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
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest : KoinTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val gameRepository: GameRepository = mock<GameRepository>()
    private lateinit var viewModel: GameViewModel

    private val testModule = module {
        single { GameViewModel(gameRepository) }
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(testModule)
        }
        viewModel = get()
    }

    @AfterTest
    fun teardown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `getGame should set game state and select first player`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game

        viewModel.game.test {
            val initialEmission = awaitItem()
            assertNull(initialEmission)

            viewModel.getGame(game.id)

            val loadedGame = awaitItem()
            assertNotNull(loadedGame)
            assertEquals(game, loadedGame)
        }

        viewModel.selectedPlayerId.test {
            assertEquals(game.players.firstOrNull()?.id, awaitItem())
        }
    }

    @Test
    fun `updateSelectedPlayer should update selectedPlayerId`() = runTest {
        viewModel.updateSelectedPlayer(2L)

        viewModel.selectedPlayerId.test {
            assertEquals(2L, awaitItem())
        }
    }

    @Test
    fun `updatePoints should correctly update STREET points`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game

        viewModel.game.test {
            val initialEmission = awaitItem()
            assertNull(initialEmission)

            viewModel.getGame(game.id)
            val loadedGame = awaitItem()
            assertNotNull(loadedGame)

            viewModel.updatePoints(1L, 1L, PlayerColumn.STREET to "20")

            val updatedGame = awaitItem()
            val streetPoints =
                updatedGame?.players?.get(0)?.columns?.get(0)?.points?.get(PlayerColumn.STREET)
            assertEquals(20, streetPoints?.pointsValue)
            assertFalse(streetPoints?.error ?: true)
        }
    }

    @Test
    fun `updatePoints should not update for invalid input`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game

        viewModel.game.test {
            val initialEmission = awaitItem()
            assertNull(initialEmission)

            viewModel.getGame(game.id)
            val loadedGame = awaitItem()
            assertNotNull(loadedGame)

            viewModel.updatePoints(1L, 1L, 1 to "invalid")
            expectNoEvents()
        }
    }

    @Test
    fun `updatePoints should correctly calculate points for row 3`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game

        viewModel.getGame(game.id)

        viewModel.game.test {
            val initialEmission = awaitItem()
            assertNull(initialEmission)

            viewModel.getGame(game.id)
            val loadedGame = awaitItem()
            assertNotNull(loadedGame)

            viewModel.updatePoints(1L, 1L, 3 to "9")
            val updatedGame = awaitItem()

            val points = updatedGame?.players?.get(0)?.columns?.get(0)?.points?.get(3)
            assertEquals(9, points?.pointsValue)
            assertFalse(points?.error ?: true)
        }
    }

    @Test
    fun `finishGame should navigate to home screen on insert errors`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game
        everySuspend { gameRepository.updatePoints(any()) } returns listOf(-1L)

        viewModel.getGame(game.id)
        viewModel.finishGame()

        viewModel.navigateEvent.test {
            val navEvent = awaitItem()
            assertEquals(NavigationRoutes.Home.route, navEvent.route)
            assertTrue(navEvent.inclusive)
        }
    }

    @Test
    fun `cancelPersistGameJob should cancel persist game job`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game
        everySuspend { gameRepository.updatePoints(any()) } returns listOf(1L)

        viewModel.getGame(game.id)
        viewModel.startSaveGameJob(1000L)

        advanceTimeBy(500L)

        viewModel.cancelSaveGameJob()

        advanceTimeBy(600L)

        verifySuspend(mode = VerifyMode.exactly(0)) { gameRepository.updatePoints(any()) }
    }

    @Test
    fun `updatePoints should not show snackbar for valid input`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game

        viewModel.getGame(game.id)
        viewModel.updatePoints(1L, 1L, 1 to "1")

        viewModel.snackbar.test {
            expectNoEvents()
        }
    }

    @Test
    fun `handleSnackbar should emit true when points are invalid`() = runTest {
        val game = createTestDiceGame()
        everySuspend { gameRepository.getDicePokerGame(1L) } returns game

        viewModel.game.test {
            val initialEmission = awaitItem()
            assertNull(initialEmission)

            viewModel.getGame(game.id)

            val loadedGame = awaitItem()
            assertNotNull(loadedGame)
            assertEquals(game.id, loadedGame.id)

            viewModel.updatePoints(
                playerId = 1L,
                columnId = 1L,
                rowValuePair = 1 to "15"
            )
            // advance time by debounce delay
            advanceTimeBy(1000L)

            val updatedGame = awaitItem()
            assertNotNull(updatedGame)

            assertEquals(
                15,
                updatedGame.players[0].columns[0].points[1]?.pointsValue
            )
            assertTrue(updatedGame.players[0].columns[0].points[1]?.error == true)
        }

        viewModel.snackbar.test {
            val showSnackbar = awaitItem()
            assertTrue(showSnackbar)
        }
    }

    @Test
    fun `finishGame should navigate to results screen on successful finish`() = runTest {
        val playerPoints = mapOf(1 to PlayerPoints(0, 1))
        val column = PlayerColumn(
            columnId = 1L,
            points = playerPoints,
            columnNumber = 1
        )
        val player = PlayerInfo(
            id = 1L,
            name = "Player 1",
            columns = listOf(column)
        )
        val game = DicePokerGame(
            id = 1L,
            players = listOf(player),
            numberOfPlayers = 1,
            numberOfColumns = 1
        )

        everySuspend { gameRepository.getDicePokerGame(1L) } returns game
        everySuspend { gameRepository.updatePoints(any()) } returns listOf(1L)

        viewModel.getGame(game.id)
        viewModel.finishGame()

        viewModel.navigateEvent.test {
            val navEvent = awaitItem()
            assertEquals("results_screen/${game.id}", navEvent.route)
        }
    }

    @Test
    fun `persistGamePeriodically should persist game state every minute`() = runTest {
        val playerPoints = mapOf(1 to PlayerPoints(0, 1))
        val column = PlayerColumn(columnId = 1L, points = playerPoints, columnNumber = 1)
        val player = PlayerInfo(id = 1L, name = "Player 1", columns = listOf(column))
        val game = DicePokerGame(
            id = 1L,
            players = listOf(player),
            numberOfPlayers = 1,
            numberOfColumns = 1
        )

        everySuspend { gameRepository.getDicePokerGame(any()) } returns game
        everySuspend { gameRepository.updatePoints(any()) } returns listOf(1L)

        viewModel.getGame(game.id)
        viewModel.startSaveGameJob(1000L)

        // Advance time by slightly more than the test interval
        testScheduler.advanceTimeBy(1100L)

        // Verify the persistence was attempted
        verifySuspend(mode = VerifyMode.exactly(1)) { gameRepository.updatePoints(any()) }

        // Cancel the job to end the test
        viewModel.cancelSaveGameJob()
    }

    private fun createTestDiceGame() = DicePokerGame(
        id = 1L,
        numberOfPlayers = 2,
        players = (1..2).map {
            PlayerInfo(
                id = it.toLong(),
                name = "Player $it",
                columns = listOf(
                    PlayerColumn(
                        columnId = 1L,
                        columnNumber = 1,
                        points = mapOf(
                            1 to PlayerPoints(
                                pointId = 0L,
                                pointsValue = 0,
                            ),
                            3 to PlayerPoints(
                                pointId = 0L,
                                pointsValue = 0,
                            ),
                            7 to PlayerPoints(
                                pointId = 0L,
                                pointsValue = 0,
                            )
                        )
                    )
                )
            )
        },
        numberOfColumns = 1
    )
}