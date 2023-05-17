package com.timurkhabibulin.myhabits.network

import android.graphics.Color
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitType

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
    //  val isSynced: Boolean
) {

    fun toHabit(): Habit =
        Habit(
            title,
            description,
            priority,
            HabitType.values()[type],
            count,
            frequency,
            "Месяц",
            Color.valueOf(color)
        ).apply {
            networkID = uid
            isSynced = true
        }

    companion object {
        fun fromHabit(habit: Habit): HabitNetworkEntity =
            HabitNetworkEntity(
                habit.networkID,
                0,
                habit.executionNumber,
                habit.name,
                habit.description,
                habit.periodNumber,
                habit.priority,
                habit.type.ordinal,
                habit.color.toArgb()
            )
    }
}