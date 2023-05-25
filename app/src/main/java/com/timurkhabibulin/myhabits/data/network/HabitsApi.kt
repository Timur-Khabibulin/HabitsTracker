package com.timurkhabibulin.myhabits.data.network

import retrofit2.http.*

interface HabitsApi {

    //TODO: Перенести токен в создание клиента
    @Headers(
        "accept: application/json",
        "Authorization: 0ea7e3b4-3431-46f6-8ead-96f161c7b4c7"
    )
    @GET("habit")
    suspend fun getHabits(): List<HabitNetworkEntity>

    @PUT("habit")
    @Headers("Authorization: 0ea7e3b4-3431-46f6-8ead-96f161c7b4c7")
    suspend fun addHabit(@Body habit: HabitNetworkEntity): String
}