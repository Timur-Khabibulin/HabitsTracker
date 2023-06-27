package com.timurkhabibulin.data.network

import com.timurkhabibulin.data.network.interceptors.AuthorizationInterceptor
import com.timurkhabibulin.data.network.json.HabitJsonDeserializer
import com.timurkhabibulin.data.network.json.HabitJsonSerializer
import com.timurkhabibulin.data.network.json.HabitUIDJsonDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.timurkhabibulin.domain.HabitsWebService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideHabitsWebService(habitsApi: HabitsApi): HabitsWebService {
        return HabitsWebServiceImpl(habitsApi)
    }

    @Singleton
    @Provides
    fun provideHabitsApi(gson: Gson, okHttpClient: OkHttpClient): HabitsApi {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(HabitsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(HabitNetworkEntity::class.java, HabitJsonDeserializer())
            .registerTypeAdapter(String::class.java, HabitUIDJsonDeserializer())
            .registerTypeAdapter(HabitNetworkEntity::class.java, HabitJsonSerializer())
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
                redactHeader("")
            })
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }

}