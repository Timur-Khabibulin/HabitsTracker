package com.timurkhabibulin.domain

import com.timurkhabibulin.domain.Entities.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun getAll(): Flow<List<Habit>>

    suspend fun add(habit: Habit): Long

    suspend fun update(habit: Habit)

   suspend fun findById(id: Long): Flow<Habit>
}