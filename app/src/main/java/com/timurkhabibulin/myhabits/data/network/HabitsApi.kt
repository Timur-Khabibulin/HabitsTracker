package com.timurkhabibulin.myhabits.data.network

import retrofit2.http.*

interface HabitsApi {

    @Headers(
        "accept: application/json",
        "Authorization: your_token"
    )
    @GET("habit")
    suspend fun getHabits(): List<HabitNetworkEntity>

    @PUT("habit")
    @Headers("Authorization: your_token")
    suspend fun addHabit(@Body habit: HabitNetworkEntity): String
}