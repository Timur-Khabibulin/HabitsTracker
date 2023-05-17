package com.timurkhabibulin.myhabits.network

import retrofit2.http.*


interface NetworkApi {
    @Headers(
        "accept: application/json",
        "Authorization: "
    )

    @GET("habit")
    suspend fun getHabits(): List<HabitNetworkEntity>

    @PUT("habit")
    @Headers("Authorization: ")
    suspend fun addHabit(@Body habit: HabitNetworkEntity): String
}