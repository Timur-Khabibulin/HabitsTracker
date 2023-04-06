package com.timurkhabibulin.myhabits.model

import android.graphics.Color

data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    val executionNumber: Int,
    val periodNumber: Int,
    val periodType: String,
    val color: Color
)

enum class HabitType {
    GOOD, BAD
}

enum class HabitSortType {
    PRIORITY, EXECUTION_NUMBER, PERIOD_NUMBER
}