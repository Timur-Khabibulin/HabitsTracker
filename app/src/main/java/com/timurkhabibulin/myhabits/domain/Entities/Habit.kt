package com.timurkhabibulin.myhabits.domain.Entities

import java.time.LocalDateTime
import java.time.temporal.ChronoField

data class Habit(
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var totalExecutionNumber: Int,
    var executionNumberInPeriod: Int,
    var periodType: PeriodType,
    var color: Int,
    var doneTimesInPeriod: Int = 0
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

    //TODO: Перенести в HabitUseCase
    fun execute() {
        totalDoneTimes++
        val currentDate = LocalDateTime.now()

        val isNewPeriod = when (periodType) {
            PeriodType.DAY -> currentDate.dayOfMonth != dateOfLastExecution.dayOfMonth ||
                    currentDate.month != dateOfLastExecution.month ||
                    currentDate.year != dateOfLastExecution.year

            PeriodType.WEEK -> currentDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR) != dateOfLastExecution.get(
                ChronoField.ALIGNED_WEEK_OF_YEAR
            ) ||
                    currentDate.year != dateOfLastExecution.year

            PeriodType.MONTH -> currentDate.month != dateOfLastExecution.month || currentDate.year != dateOfLastExecution.year

            PeriodType.YEAR -> currentDate.year != dateOfLastExecution.year
        }

        if (!isNewPeriod) doneTimesInPeriod++
        else {
            dateOfLastExecution = currentDate
            doneTimesInPeriod = 1
        }

    }
}