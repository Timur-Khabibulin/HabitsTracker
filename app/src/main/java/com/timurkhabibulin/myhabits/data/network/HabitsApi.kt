package com.timurkhabibulin.myhabits.data.network

import retrofit2.http.*

interface HabitsApi {

    @Headers("accept: application/json")
    @GET("habit")
    suspend fun getHabits(): List<HabitNetworkEntity>

    @PUT("habit")
    suspend fun addHabit(@Body habit: HabitNetworkEntity): String
}