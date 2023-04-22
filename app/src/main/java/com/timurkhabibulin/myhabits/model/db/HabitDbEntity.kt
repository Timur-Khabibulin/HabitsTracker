package com.timurkhabibulin.myhabits.model.db

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitType

@Entity(tableName = "Habits")
data class HabitDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var executionNumber: Int,
    var periodNumber: Int,
    var periodType: String,
    var color: Color
) {
    //   @PrimaryKey(autoGenerate = true)
    // val id: Int? = 0
    fun toHabit(): Habit =
        Habit(
            id,
            name,
            description,
            priority,
            type,
            executionNumber,
            periodNumber,
            periodType,
            color
        )

    companion object {
        fun fromHabit(habit: Habit): HabitDbEntity =
            HabitDbEntity(
               habit.id,
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.executionNumber,
                habit.periodNumber,
                habit.periodType,
                habit.color
            )
    }
}