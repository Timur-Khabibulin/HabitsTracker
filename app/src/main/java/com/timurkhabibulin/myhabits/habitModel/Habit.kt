package com.timurkhabibulin.myhabits.habitModel

import android.graphics.Color

enum class HabitType{
    GOOD,BAD
}

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