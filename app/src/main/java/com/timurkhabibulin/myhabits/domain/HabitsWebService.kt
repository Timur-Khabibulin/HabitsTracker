package com.timurkhabibulin.myhabits.domain

import com.timurkhabibulin.myhabits.domain.Entities.Habit

interface HabitsWebService {

    suspend fun getHabits(): List<Habit>

    suspend fun addHabit(habit: Habit): String
}