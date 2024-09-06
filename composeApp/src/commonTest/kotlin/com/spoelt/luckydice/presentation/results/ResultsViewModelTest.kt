package com.spoelt.luckydice.presentation.results

import app.cash.turbine.test
import com.spoelt.luckydice.domain.model.DicePokerGame
import com.spoelt.luckydice.domain.model.PlayerColumn
import com.spoelt.luckydice.domain.model.PlayerInfo
import com.spoelt.luckydice.domain.model.PlayerPoints
import com.spoelt.luckydice.domain.model.PlayerRanking
import com.spoelt.luckydice.domain.model.PlayerResult
import com.spoelt.luckydice.domain.repository.GameRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ResultsViewModelTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private val gameRepository: GameRepository = mock<GameRepository>()
    private lateinit var viewModel: ResultsViewModel

    private val testModule = module {
        single { ResultsViewModel(gameRepository) }
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
    fun getPlayerPoints_normalGame_correctRankings() = runTest {
        // Arrange
        val gameId = 1L
        val game = createNormalGame(gameId)
        everySuspend { gameRepository.getDicePokerGame(gameId) } returns game

        // Act & Assert
        viewModel.players.test {
            viewModel.getPlayerPoints(gameId)

            val initialEmission = awaitItem()
            assertEquals(true, initialEmission.isEmpty())

            val players = awaitItem()
            assertEquals(game.players.size, players.size)

            val firstPlace = PlayerResult("Player 3", 3, PlayerRanking.FIRST_PLACE)
            assertEquals(firstPlace, players[0])

            val secondPlace = PlayerResult("Player 2", 2, PlayerRanking.SECOND_PLACE)
            assertEquals(secondPlace, players[1])

            val thirdPlace = PlayerResult("Player 1", 1, PlayerRanking.THIRD_PLACE)
            assertEquals(thirdPlace, players[2])
        }
    }

    @Test
    fun getPlayerPoints_threeWayTie_correctRankings() = runTest {
        // Arrange
        val gameId = 2L
        val game = createThreeWayTie(gameId)
        everySuspend { gameRepository.getDicePokerGame(gameId) } returns game

        // Act & Assert
        viewModel.players.test {
            viewModel.getPlayerPoints(gameId)

            val initialEmission = awaitItem()
            assertEquals(true, initialEmission.isEmpty())

            val players = awaitItem()
            assertEquals(game.players.size, players.size)
            assertTrue(players.all { it.ranking == PlayerRanking.FIRST_PLACE })
        }
    }

    @Test
    fun getPlayerPoints_tieForSecondPlace_correctRankings() = runTest {
        // Arrange
        val gameId = 3L
        val game = createGameWithTieForSecondPlace(gameId)
        everySuspend { gameRepository.getDicePokerGame(gameId) } returns game

        // Act & Assert
        viewModel.players.test {
            viewModel.getPlayerPoints(gameId)

            val initialEmission = awaitItem()
            assertEquals(true, initialEmission.isEmpty())

            val players = awaitItem()
            assertEquals(game.players.size, players.size)
            assertEquals(PlayerResult("Player 3", 4, PlayerRanking.FIRST_PLACE), players[0])

            val otherPlayers = players.withoutFirstPlace()
            assertTrue(otherPlayers.all { it.name == "Player 2" || it.name == "Player 1" })
            assertTrue(otherPlayers.all { it.ranking == PlayerRanking.SECOND_PLACE })
        }
    }

    @Test
    fun getPlayerPoints_moreThanThreePlayers_correctRankings() = runTest {
        // Arrange
        val gameId = 5L
        val game = createGameWithFourPlayers(gameId)
        everySuspend { gameRepository.getDicePokerGame(gameId) } returns game

        // Act & Assert
        viewModel.players.test {
            viewModel.getPlayerPoints(gameId)

            val initialEmission = awaitItem()
            assertEquals(true, initialEmission.isEmpty())

            val players = awaitItem()
            assertEquals(game.players.size, players.size)
            assertEquals(PlayerResult("Player 1", 3, PlayerRanking.FIRST_PLACE), players[0])
            assertEquals(PlayerResult("Player 2", 2, PlayerRanking.SECOND_PLACE), players[1])
            assertEquals(PlayerResult("Player 3", 1, PlayerRanking.THIRD_PLACE), players[2])
            assertEquals(PlayerResult("Player 4", 0, PlayerRanking.OTHER), players[3])
        }
    }

    @Test
    fun getPlayerPoints_twoColumnsGame_correctRankings() = runTest {
        // Arrange
        val gameId = 6L
        val game = createTwoColumnGameWithTwoPlayers(gameId)
        everySuspend { gameRepository.getDicePokerGame(gameId) } returns game

        // Act & Assert
        viewModel.players.test {
            viewModel.getPlayerPoints(gameId)

            val initialEmission = awaitItem()
            assertEquals(true, initialEmission.isEmpty())

            val players = awaitItem()
            assertEquals(game.players.size, players.size)
            assertEquals(PlayerResult("Player 1", 2, PlayerRanking.FIRST_PLACE), players[0])
            assertEquals(PlayerResult("Player 2", 0, PlayerRanking.SECOND_PLACE), players[1])
        }
    }

    @Test
    fun getPlayerPoints_nonExistentGame_noEventsEmitted() = runTest {
        // Arrange
        val nonExistentGameId = 999L
        everySuspend { gameRepository.getDicePokerGame(999L) } returns null

        // Act & Assert
        viewModel.players.test {
            viewModel.getPlayerPoints(nonExistentGameId)

            val initialEmission = awaitItem()
            assertEquals(true, initialEmission.isEmpty())

            expectNoEvents() // No update for players should be emitted
        }

        viewModel.goBackEvent.test {
            assertEquals(Unit, awaitItem())
        }
    }

    private fun createNormalGame(id: Long): DicePokerGame = createTestDicePokerGame(
        gameId = id,
        players = listOf(
            PlayerInfo(id = 1, name = "Player 1", columns = createColumns(listOf(1, 0, 0))),
            PlayerInfo(id = 2, name = "Player 2", columns = createColumns(listOf(0, 1, 0))),
            PlayerInfo(id = 3, name = "Player 3", columns = createColumns(listOf(0, 0, 1)))
        )
    )

    private fun createThreeWayTie(id: Long): DicePokerGame = createTestDicePokerGame(
        gameId = id,
        players = listOf(
            PlayerInfo(id = 1, name = "Player 1", columns = createColumns(listOf(3, 1, 1))),
            PlayerInfo(id = 2, name = "Player 2", columns = createColumns(listOf(3, 1, 1))),
            PlayerInfo(id = 3, name = "Player 3", columns = createColumns(listOf(1, 0, 2)))
        )
    )

    private fun createGameWithTieForSecondPlace(id: Long): DicePokerGame = createTestDicePokerGame(
        gameId = id,
        players = listOf(
            PlayerInfo(id = 1, name = "Player 1", columns = createColumns(listOf(3, 1, 1))),
            PlayerInfo(id = 2, name = "Player 2", columns = createColumns(listOf(3, 1, 1))),
            PlayerInfo(id = 3, name = "Player 3", columns = createColumns(listOf(6, 0, 2)))
        )
    )

    private fun createTwoColumnGameWithTwoPlayers(id: Long): DicePokerGame =
        createTestDicePokerGame(
            gameId = id,
            numberOfPlayers = 2,
            numberOfColumns = 2,
            players = listOf(
                PlayerInfo(id = 1, name = "Player 1", columns = createColumns(listOf(5, 5))),
                PlayerInfo(id = 2, name = "Player 2", columns = createColumns(listOf(1, 4))),
            )
        )

    private fun createGameWithFourPlayers(id: Long): DicePokerGame = createTestDicePokerGame(
        gameId = id,
        numberOfPlayers = 4,
        numberOfColumns = 3,
        players = listOf(
            PlayerInfo(id = 1, name = "Player 1", columns = createColumns(listOf(0, 0, 1))),
            PlayerInfo(id = 2, name = "Player 2", columns = createColumns(listOf(0, 1, 0))),
            PlayerInfo(id = 3, name = "Player 3", columns = createColumns(listOf(1, 0, 0))),
            PlayerInfo(id = 4, name = "Player 4", columns = createColumns(listOf(0, 0, 0)))
        )
    )

    private fun List<PlayerResult>.withoutFirstPlace() =
        this.filter { it.ranking != PlayerRanking.FIRST_PLACE }

    private fun createColumns(points: List<Int>): List<PlayerColumn> =
        points.mapIndexed { index, point ->
            PlayerColumn(
                columnId = index.toLong(),
                columnNumber = index + 1,
                points = mapOf(
                    1 to PlayerPoints(
                        pointId = 0L,
                        pointsValue = point,
                    )
                )
            )
        }

    private fun createTestDicePokerGame(
        gameId: Long,
        numberOfPlayers: Int = 3,
        numberOfColumns: Int = 3,
        players: List<PlayerInfo>
    ) = DicePokerGame(
        id = gameId,
        numberOfPlayers = numberOfPlayers,
        players = players,
        numberOfColumns = numberOfColumns
    )
}