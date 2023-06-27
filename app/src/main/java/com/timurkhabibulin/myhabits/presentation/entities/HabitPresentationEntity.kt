package com.timurkhabibulin.myhabits.presentation.entities

import android.graphics.Color
import com.timurkhabibulin.domain.Entities.Habit
import com.timurkhabibulin.domain.Entities.HabitType
import com.timurkhabibulin.domain.Entities.PeriodType

data class HabitPresentationEntity(
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var totalExecutionNumber: Int,
    var executionNumberInPeriod: Int,
    var periodType: PeriodType,
    var color: Color,
    var id: Long = 0
) {

    fun toHabit(): Habit =
        Habit(
            name,
            description,
            priority,
            type,
            totalExecutionNumber,
            executionNumberInPeriod,
            periodType,
            color.toArgb()
        )

    companion object {
        fun fromHabit(habit: Habit): HabitPresentationEntity =
            HabitPresentationEntity(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.totalExecutionNumber,
                habit.executionNumberInPeriod,
                habit.periodType,
                Color.valueOf(habit.color),
                habit.internalID
            )
    }
}