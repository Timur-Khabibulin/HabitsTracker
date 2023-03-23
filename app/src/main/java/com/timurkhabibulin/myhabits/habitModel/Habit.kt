package com.timurkhabibulin.myhabits.habitModel

import android.graphics.Color


data class Habit(
    val name: String,
    val description: String,
    val priority: Int,
    val type: String,
    val executionNumber: Int,
    val periodNumber: Int,
    val periodType: String,
    val color: Color
)