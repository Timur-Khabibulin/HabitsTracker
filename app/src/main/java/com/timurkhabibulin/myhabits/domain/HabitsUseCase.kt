package com.timurkhabibulin.myhabits.domain

import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitsUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val habitsWebService: HabitsWebService,
    private val dispatcher: CoroutineDispatcher
) {
    private val mutableDoneHabitMessage = MutableSharedFlow<String>()
    val doneHabitMessage: Flow<String> = mutableDoneHabitMessage
    private var messageWasSend = false

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
        messageWasSend = false
        withContext(dispatcher) {
            findById(id).collect {
                if (!messageWasSend) {
                    it.execute()
                    update(it)
                    val delta = it.executionNumberInPeriod - it.doneTimesInPeriod

                    //Todo: Возвзращать enum либо класс
                    mutableDoneHabitMessage.emit(
                        if (delta > 0) {
                            when (it.type) {
                                HabitType.GOOD -> "Стоит выполнить еще $delta раз"
                                HabitType.BAD -> "Можете выполнить еще $delta раз"
                            }
                        } else {
                            when (it.type) {
                                HabitType.GOOD -> "You are breathtaking!"
                                HabitType.BAD -> "Хватит это делать"
                            }
                        }
                    )
                    messageWasSend = true
                }
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

}