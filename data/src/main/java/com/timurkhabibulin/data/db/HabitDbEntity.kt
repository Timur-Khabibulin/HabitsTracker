package com.timurkhabibulin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timurkhabibulin.domain.Entities.Habit
import com.timurkhabibulin.domain.Entities.HabitType
import com.timurkhabibulin.domain.Entities.PeriodType


@Entity(tableName = "Habits")
data class HabitDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    var description: String,
    var priority: Int,
    var type: String,
    var executionNumber: Int,
    var periodNumber: Int,
    var periodType: PeriodType,
    var color: Int,
) {
    var isSynced: Boolean = false
    var networkID: String = ""
    var doneNumber: Int = 0

    fun toHabit(): Habit {
        val converted = Habit(
            name,
            description,
            priority,
            HabitType.valueOf(type),
            executionNumber,
            periodNumber,
            periodType,
            color,
            doneNumber
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
                habit.type.toString(),
                habit.totalExecutionNumber,
                habit.executionNumberInPeriod,
                habit.periodType,
                habit.color,
            ).apply {
                networkID = habit.networkID
                isSynced = habit.isSynced
                doneNumber = habit.totalDoneTimes
            }
    }
}