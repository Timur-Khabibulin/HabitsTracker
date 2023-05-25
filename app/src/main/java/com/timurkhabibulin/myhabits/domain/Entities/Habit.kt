package com.timurkhabibulin.myhabits.domain.Entities

import android.graphics.Color
import java.time.LocalDateTime
import java.time.temporal.ChronoField

data class Habit(
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var totalExecutionNumber: Int,
    var executionNumberInPeriod: Int,
    var periodType: String,
    var color: Color,
    var doneTimesInPeriod: Int
) {
    var internalID: Long = 0
    var networkID: String = ""
    var isSynced: Boolean = false
    var totalDoneTimes: Int = 0
        private set

    private var dateOfLastExecution: LocalDateTime

    init {
        dateOfLastExecution = LocalDateTime.now()
    }

    fun execute() {
        totalDoneTimes++
        val currentDate = LocalDateTime.now()

        val isNewPeriod = when (periodType) {
            "День" -> currentDate.dayOfMonth != dateOfLastExecution.dayOfMonth ||
                    currentDate.month != dateOfLastExecution.month ||
                    currentDate.year != dateOfLastExecution.year

            "Неделя" -> currentDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR) != dateOfLastExecution.get(
                ChronoField.ALIGNED_WEEK_OF_YEAR
            ) ||
                    currentDate.year != dateOfLastExecution.year //TODO: number of week

            "Месяц" -> currentDate.month != dateOfLastExecution.month || currentDate.year != dateOfLastExecution.year

            "Год" -> currentDate.year != dateOfLastExecution.year
            else -> false
        }

        if (!isNewPeriod) doneTimesInPeriod++
        else {
            dateOfLastExecution = currentDate
            doneTimesInPeriod = 1
        }

    }
}