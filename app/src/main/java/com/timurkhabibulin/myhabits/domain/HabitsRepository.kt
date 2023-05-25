package com.timurkhabibulin.myhabits.domain

import com.timurkhabibulin.myhabits.domain.Entities.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun getAll(): Flow<List<Habit>>

    suspend fun add(habit: Habit): Long

    suspend fun update(habit: Habit)

   suspend fun findById(id: Long): Flow<Habit>
}