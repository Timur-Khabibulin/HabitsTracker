package com.timurkhabibulin.data.network

import android.util.Log
import com.timurkhabibulin.domain.Entities.Habit
import com.timurkhabibulin.domain.HabitsWebService

class HabitsWebServiceImpl(
    private val habitsApi: HabitsApi
) : HabitsWebService {

    override suspend fun getHabits(): List<Habit> {
        try {
            return habitsApi.getHabits().map { it.toHabit() }
        } catch (e: Exception) {
            Log.e("HabitsWebService", "Trying to get habits habits from server threw $e")
        }
        return listOf()
    }

    override suspend fun addHabit(habit: Habit): String {
        try {
            return habitsApi.addHabit(HabitNetworkEntity.fromHabit(habit))
        } catch (e: Exception) {
            Log.e("HabitsWebService", "Trying to add a habit on the server threw $e")
        }
        return ""
    }

}