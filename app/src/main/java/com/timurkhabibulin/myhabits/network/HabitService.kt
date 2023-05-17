package com.timurkhabibulin.myhabits.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.timurkhabibulin.myhabits.db.HabitsRepository
import com.timurkhabibulin.myhabits.model.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HabitService(
    private val habitsRepository: HabitsRepository,
    private val networkApi: NetworkApi
) {
    private var isSynced = false

    fun getAll(): Flow<List<Habit>> = habitsRepository.getAll()

    suspend fun update(habit: Habit) {
        habitsRepository.update(habit)
        addToServer(habit)
    }

    suspend fun addHabit(habit: Habit) {
        val id = habitsRepository.insert(habit)
        addToServer(habit.apply { internalID = id })
    }

    fun findById(id: Long): LiveData<Habit> = habitsRepository.findById(id)


    suspend fun synchronizeWithNetwork() {
        habitsRepository.getAll().collect {
            if (!isSynced) {
                withContext(Dispatchers.Default) {
                    networkApi.getHabits().map { it.toHabit() }
                }.let { habitsFromServer ->
                    habitsFromServer.forEach { netHabit ->
                        if (!it.any { dbHabit -> dbHabit.networkID == netHabit.networkID }
                        ) habitsRepository.insert(netHabit)
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
        try {
            withContext(Dispatchers.Default) {
                networkApi.addHabit(HabitNetworkEntity.fromHabit(habit))
            }.let {
                habitsRepository.update(habit.apply {
                    networkID = it
                    isSynced = true
                })
            }
        } catch (e: Exception) {
            Log.e("Network", "Trying to add a habit on the server threw $e")
        }
    }

}