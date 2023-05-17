package com.timurkhabibulin.myhabits.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.timurkhabibulin.myhabits.model.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitsRepository(
    private val habitDao: HabitDao,
) {

    fun getAll(): Flow<List<Habit>> {
        return habitDao.getAll().map { it.map { x -> x.toHabit() } }
    }

    suspend fun insert(habit: Habit):Long {
       return habitDao.insert(HabitDbEntity.fromHabit(habit))
    }

    suspend fun update(habit: Habit) {
        habitDao.update(HabitDbEntity.fromHabit(habit))
    }

    fun findById(id: Long): LiveData<Habit> {
        return habitDao.findById(id).map { it.toHabit() }
    }
}