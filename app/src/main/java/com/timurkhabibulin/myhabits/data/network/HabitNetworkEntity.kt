package com.timurkhabibulin.myhabits.data.network

import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.domain.Entities.PeriodType

data class HabitNetworkEntity(
    val uid: String,
    val date: Int,
    val count: Int,
    val title: String,
    val description: String,
    val frequency: Int,
    val priority: Int,
    val type: Int,
    val color: Int
) {

    fun toHabit(): Habit =
        Habit(
            title,
            description,
            priority,
            HabitType.values()[type],
            count,
            frequency,
            PeriodType.MONTH,
            color,
            date
        ).apply {
            networkID = uid
            isSynced = true
        }

    companion object {
        fun fromHabit(habit: Habit): HabitNetworkEntity =
            HabitNetworkEntity(
                habit.networkID,
                habit.totalDoneTimes,
                habit.totalExecutionNumber,
                habit.name,
                habit.description,
                habit.executionNumberInPeriod,
                habit.priority,
                habit.type.ordinal,
                habit.color//.toArgb()
            )
    }
}