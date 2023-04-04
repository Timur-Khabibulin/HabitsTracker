package com.timurkhabibulin.myhabits.model

typealias HabitsListener = (habits: List<Habit>) -> Unit

object HabitService {
    private var allHabits = mutableListOf<Habit>()
    private var idCount = 0

    fun addHabit(habit: Habit) {
        allHabits.add(habit)
        idCount++
    }

    fun getAllHabits(): List<Habit> {
        return mutableListOf<Habit>().apply { addAll(allHabits) }
    }

    fun getHabit(ID: Int): Habit? = allHabits.find { x -> x.id == ID }

    fun getNextId() = ++idCount

    fun changeItem(itemID: Int, newValue: Habit) {
        val index = allHabits.indexOfFirst { x -> x.id == itemID }
        allHabits[index] = newValue
    }

    fun loadHabits(x: (List<Habit>) -> Unit) = x(allHabits)
}