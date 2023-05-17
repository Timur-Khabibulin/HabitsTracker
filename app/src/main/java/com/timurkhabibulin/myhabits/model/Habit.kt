package com.timurkhabibulin.myhabits.model

import android.graphics.Color

data class Habit(
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var executionNumber: Int,
    var periodNumber: Int,
    var periodType: String,
    var color: Color,
) {
    var internalID: Long = 0
    var networkID: String = ""
    var isSynced: Boolean = false
}