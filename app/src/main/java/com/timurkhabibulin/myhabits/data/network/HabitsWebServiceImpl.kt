package com.timurkhabibulin.myhabits.data.network

import android.util.Log
import com.timurkhabibulin.myhabits.domain.HabitsWebService
import com.timurkhabibulin.myhabits.domain.Entities.Habit

class HabitsWebServiceImpl(
    private val habitsApi: HabitsApi
) : HabitsWebService {

    override suspend fun getHabits(): List<Habit> {
        return habitsApi.getHabits().map { it.toHabit() }
    }

    override suspend fun addHabit(habit: Habit): String {
        try {
            return habitsApi.addHabit(HabitNetworkEntity.fromHabit(habit))
        } catch (e: Exception) {
            Log.e("HabitsWebService", "Trying to add a habit on the server threw $e")
        }
        return ""//TODO return something if request is bad or exception
    }

}