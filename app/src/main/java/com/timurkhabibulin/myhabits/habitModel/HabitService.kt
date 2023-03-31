package com.timurkhabibulin.myhabits.habitModel

typealias HabitsListener = (habits: List<Habit>) -> Unit

object HabitService {
    private var listeners = mutableListOf<HabitsListener>()
    private var allHabits = mutableListOf<Habit>()
    private var idCount = 0

    fun addHabit(habit: Habit) {
        allHabits.add(habit)
        idCount++
        notifyChanges()
    }

    fun getAllHabits(): List<Habit> {
        return mutableListOf<Habit>().apply { addAll(allHabits) }
    }

    fun getHabit(ID: Int): Habit? = allHabits.find { x -> x.id == ID }

    fun getNextId() = ++idCount

    fun addListener(listener: HabitsListener, habitType: HabitType) {
        listeners.add(listener)
        listener.invoke(allHabits.filter { x -> x.type == habitType })
    }

    fun changeItem(itemID: Int, newValue: Habit) {
        val index = allHabits.indexOfFirst { x -> x.id == itemID }
        allHabits[index] = newValue
    }

    fun removeListener(listener: HabitsListener) {
        listeners.remove(listener)
        listener.invoke(allHabits)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(allHabits) }
}