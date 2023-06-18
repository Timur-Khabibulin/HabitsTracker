package com.timurkhabibulin.myhabits.domain.Entities

import java.time.LocalDateTime

data class Habit(
    val name: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    val totalExecutionNumber: Int,
    val executionNumberInPeriod: Int,
    val periodType: PeriodType,
    val color: Int,
    var doneTimesInPeriod: Int = 0
) {
    var internalID: Long = 0
    var networkID: String = ""
    var isSynced: Boolean = false
    var totalDoneTimes: Int = 0
    var dateOfLastExecution: LocalDateTime = LocalDateTime.now()
}