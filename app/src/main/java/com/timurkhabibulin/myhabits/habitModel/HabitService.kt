package com.timurkhabibulin.myhabits.habitModel

typealias HabitsListener = (habits: List<Habit>) -> Unit

object HabitService {
    private var listeners = mutableListOf<HabitsListener>()
    private var habits = mutableListOf<Habit>()

    fun addHabit(habit: Habit) {
        habits.add(habit)
        notifyChanges()
    }

    fun getAllHabits(): List<Habit> {
        return habits
    }

    fun getHabit(position: Int): Habit = habits[position]

    fun addListener(listener: HabitsListener) {
        listeners.add(listener)
        listener.invoke(habits)
    }

    fun changeItem(index: Int, newValue: Habit) {
        habits[index] = newValue
    }

    fun removeListener(listener: HabitsListener) {
        listeners.remove(listener)
        listener.invoke(habits)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(habits) }
}