package com.timurkhabibulin.myhabits.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.timurkhabibulin.myhabits.domain.HabitsWebService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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
            .addInterceptor(Interceptor {
                val original: Request = it.request()

                val request: Request = original.newBuilder()
                    .header("Authorization", "0ea7e3b4-3431-46f6-8ead-96f161c7b4c7")
                    .method(original.method, original.body)
                    .build()

                return@Interceptor it.proceed(request)
            })
            .build()
    }

}