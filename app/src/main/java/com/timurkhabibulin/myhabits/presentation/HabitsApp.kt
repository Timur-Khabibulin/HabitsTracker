package com.timurkhabibulin.myhabits.presentation

import android.app.Application
import com.timurkhabibulin.myhabits.data.DaggerDataComponent
import com.timurkhabibulin.myhabits.data.DataComponent
import kotlinx.coroutines.Dispatchers

class HabitsApp : Application() {

    lateinit var dataComponent: DataComponent

    override fun onCreate() {
        super.onCreate()

        dataComponent = DaggerDataComponent.builder()
            .dispatchersModule(DispatchersModule(Dispatchers.IO))
            .contextModule(ContextModule(this))
            .build()
    }
}