package com.timurkhabibulin.myhabits.domain

import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.Entities.HabitWasDone
import com.timurkhabibulin.myhabits.domain.Entities.PeriodType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import javax.inject.Inject

class HabitsUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val habitsWebService: HabitsWebService,
    private val dispatcher: CoroutineDispatcher
) {
    private val mutableDoneHabit = MutableSharedFlow<HabitWasDone>()
    val doneHabit: SharedFlow<HabitWasDone> = mutableDoneHabit

    private var isSynced = false

    suspend fun getAll(): Flow<List<Habit>> =
        withContext(dispatcher) { return@withContext habitsRepository.getAll() }

    suspend fun addHabit(habit: Habit) {
        withContext(dispatcher) {
            val id = habitsRepository.add(habit)
            addToServer(habit.apply { internalID = id })
        }
    }

    suspend fun habitWasDone(id: Long) {
        withContext(dispatcher) {
            findById(id).collect {
                it.execute()
                update(it)
                mutableDoneHabit.emit(
                    HabitWasDone(
                        it.type,
                        it.executionNumberInPeriod - it.doneTimesInPeriod
                    )
                )
                cancel()
            }
        }
    }

    suspend fun update(habit: Habit) {
        withContext(dispatcher) {
            habitsRepository.update(habit)
            addToServer(habit)
        }
    }

    suspend fun findById(id: Long): Flow<Habit> {
        return withContext(dispatcher) {
            return@withContext habitsRepository.findById(id)
        }
    }

    suspend fun synchronizeWithNetwork() {
        habitsRepository.getAll().collect {
            if (!isSynced) {
                withContext(dispatcher) {
                    habitsWebService.getHabits()
                }.let { habitsFromServer ->
                    habitsFromServer.forEach { netHabit ->
                        if (!it.any { dbHabit -> dbHabit.networkID == netHabit.networkID }
                        ) habitsRepository.add(netHabit)
                    }

                    it.forEach { dbHabit ->
                        if (!dbHabit.isSynced) {
                            addToServer(dbHabit)
                        }
                    }
                }

                isSynced = true
            }
        }
    }

    private suspend fun addToServer(habit: Habit) {
        withContext(dispatcher) {
            habitsWebService.addHabit(habit)
        }.let {
            habitsRepository.update(habit.apply {
                networkID = it
                isSynced = true
            })
        }
    }

    private fun Habit.execute() {
        totalDoneTimes++
        val currentDate = LocalDateTime.now()

        val isNewPeriod = when (periodType) {
            PeriodType.DAY -> currentDate.dayOfMonth != dateOfLastExecution.dayOfMonth ||
                    currentDate.month != dateOfLastExecution.month ||
                    currentDate.year != dateOfLastExecution.year

            PeriodType.WEEK -> currentDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR) != dateOfLastExecution.get(
                ChronoField.ALIGNED_WEEK_OF_YEAR
            ) || currentDate.year != dateOfLastExecution.year

            PeriodType.MONTH -> currentDate.month != dateOfLastExecution.month || currentDate.year != dateOfLastExecution.year

            PeriodType.YEAR -> currentDate.year != dateOfLastExecution.year
        }

        if (!isNewPeriod) doneTimesInPeriod++
        else {
            dateOfLastExecution = currentDate
            doneTimesInPeriod = 1
        }

    }

}