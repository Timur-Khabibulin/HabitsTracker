package com.timurkhabibulin.domain

import com.timurkhabibulin.domain.Entities.Habit

interface HabitsWebService {

    suspend fun getHabits(): List<Habit>

    suspend fun addHabit(habit: Habit): String
}