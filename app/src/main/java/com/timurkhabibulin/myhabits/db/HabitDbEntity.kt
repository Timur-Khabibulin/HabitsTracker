package com.timurkhabibulin.myhabits.db

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitType

@Entity(tableName = "Habits")
data class HabitDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var executionNumber: Int,
    var periodNumber: Int,
    var periodType: String,
    var color: Color,
) {
    var isSynced: Boolean = false
    var networkID: String = ""

    fun toHabit(): Habit {
        val converted = Habit(
            name,
            description,
            priority,
            type,
            executionNumber,
            periodNumber,
            periodType,
            color,
        )
        converted.networkID = networkID
        converted.internalID = id
        converted.isSynced = isSynced

        return converted
    }

    companion object {
        fun fromHabit(habit: Habit): HabitDbEntity =
            HabitDbEntity(
                habit.internalID,
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.executionNumber,
                habit.periodNumber,
                habit.periodType,
                habit.color,
            ).apply {
                networkID=habit.networkID
                isSynced=habit.isSynced
            }
    }
}