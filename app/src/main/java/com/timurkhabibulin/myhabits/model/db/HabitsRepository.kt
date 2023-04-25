package com.timurkhabibulin.myhabits.model.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.timurkhabibulin.myhabits.model.Habit

class HabitsRepository(
    private val habitDao: HabitDao
) {

     fun getAll(): LiveData<List<Habit>> {
        return Transformations.map(habitDao.getAll()) {
            return@map it.map { x -> x.toHabit() }
        }
    }

    suspend fun insert(habit: Habit) {
        val entity = HabitDbEntity.fromHabit(habit)
        habitDao.insert(entity)
    }

    suspend fun update(habit: Habit) {
        habitDao.update(HabitDbEntity.fromHabit(habit))
    }

     fun findById(id: Int): LiveData<Habit> {
        return Transformations.map(habitDao.findById(id)) {
            return@map it.toHabit()
        }
    }

}