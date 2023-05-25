package com.timurkhabibulin.myhabits.data.db

import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitsRepositoryImpl(
    private val habitDao: HabitDao,
) : HabitsRepository {

    override fun getAll(): Flow<List<Habit>> {
        return habitDao.getAll().map { it.map { x -> x.toHabit() } }
    }

    override suspend fun add(habit: Habit): Long {
        return habitDao.insert(HabitDbEntity.fromHabit(habit))
    }

    override suspend fun update(habit: Habit) {
        habitDao.update(HabitDbEntity.fromHabit(habit))
    }

    override suspend fun findById(id: Long): Flow<Habit> {
        return habitDao.findById(id).map { it.toHabit() }
    }
}