package com.timurkhabibulin.myhabits.presentation

import android.app.Application

class HabitsApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .dispatchersModule(DispatchersModule())
            .contextModule(ContextModule(this))
            .build()

    }
}