package com.timurkhabibulin.myhabits.domain

import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.domain.Entities.PeriodType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
internal class HabitsUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var habitsUseCase: HabitsUseCase
    private lateinit var mockHabitsRepository: HabitsRepository
    private lateinit var mockHabitsWebService: HabitsWebService
    private lateinit var testHabit: Habit

    @Before
    fun setUp() {
        mockHabitsRepository = mock()
        mockHabitsWebService = mock()
        habitsUseCase = HabitsUseCase(
            mockHabitsRepository,
            mockHabitsWebService,
            dispatcher
        )
        testHabit = Habit(
            "testName",
            "test",
            1,
            HabitType.BAD,
            3,
            2,
            PeriodType.MONTH,
            12323,
            0
        )
    }

    @Test
    fun `should return all habits`() = runTest(dispatcher) {
        val expected = listOf(testHabit.copy())
        val actual = mutableListOf<Habit>()

        whenever(mockHabitsRepository.getAll()).thenReturn(flow { emit(expected) })
        whenever(mockHabitsWebService.getHabits()).thenReturn(expected)

        habitsUseCase.getAll().collect {
            actual.addAll(it)
        }

        advanceUntilIdle()

        assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
    }

    @Test
    fun `should add new habit`() = runTest(dispatcher) {
        val expected = listOf(testHabit.copy())
        val actual = mutableListOf<Habit>()

        whenever(mockHabitsRepository.getAll()).thenReturn(flow { actual })

        whenever(mockHabitsRepository.add(any())).doAnswer {
            actual.add(it.arguments[0] as Habit)
            return@doAnswer 0
        }

        whenever((mockHabitsWebService.addHabit(any()))).doAnswer {
            return@doAnswer "123"
        }

        habitsUseCase.addHabit(testHabit.copy())
        habitsUseCase.getAll().collect {
            actual.addAll(it)
        }

        advanceUntilIdle()

        assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
    }

    @Test
    fun `should find habit by id`() = runTest(dispatcher) {
        val expected = testHabit.copy().apply { internalID = 3 }
        var actual = expected.copy().apply { internalID = 999 }
        val id = expected.internalID

        whenever(mockHabitsRepository.findById(any())).doAnswer {
            return@doAnswer flow { expected }
        }

        habitsUseCase.findById(id).collect { actual = it }

        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun `should update habit`() = runTest(dispatcher) {
        val id = "123"
        val expected = testHabit.copy().apply {
            name = "expected name"
            networkID = id
            isSynced = true
        }
        var actual = expected.copy().apply {
            name = "wrong name"
            networkID = "0"
            isSynced = false
        }

        whenever(mockHabitsRepository.update(any())).doAnswer {
            actual = it.arguments[0] as Habit
        }

        whenever(mockHabitsWebService.addHabit(any())).doAnswer {
            return@doAnswer id
        }

        habitsUseCase.update(expected.copy())

        advanceUntilIdle()

        assertEquals(expected, actual)
    }
}