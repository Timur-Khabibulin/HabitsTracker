package com.timurkhabibulin.myhabits.presentation

import android.app.Application
import kotlinx.coroutines.Dispatchers

class HabitsApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .dispatchersModule(DispatchersModule(Dispatchers.IO))
            .contextModule(ContextModule(this))
            .build()

    }
}