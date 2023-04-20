package com.timurkhabibulin.myhabits.model

import android.graphics.Color

data class Habit(
    val id: Int,
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var executionNumber: Int,
    var periodNumber: Int,
    var periodType: String,
    var color: Color
)