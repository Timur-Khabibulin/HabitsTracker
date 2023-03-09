package com.timurkhabibulin.myhabits

typealias HabitsListener = (habits: List<Habit>) -> Unit

object HabitService {
    private var listeners = mutableListOf<HabitsListener>()
    private var habits = mutableListOf<Habit>()

    fun addHabit(habit: Habit) {
        habits.add(habit)
        notifyChanges()
    }

    fun getHabits(): List<Habit> {
        return habits
    }

    fun addListener(listener: HabitsListener) {
        listeners.add(listener)
        listener.invoke(habits)
    }

    fun removeListener(listener: HabitsListener) {
        listeners.remove(listener)
        listener.invoke(habits)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(habits) }
}