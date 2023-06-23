package com.timurkhabibulin.myhabits.presentation

import com.timurkhabibulin.myhabits.data.db.DatabaseModule
import com.timurkhabibulin.myhabits.data.network.NetworkModule
import com.timurkhabibulin.myhabits.presentation.view.fragments.HabitEditingFragment
import com.timurkhabibulin.myhabits.presentation.view.fragments.MenuFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ContextModule::class,
        DispatchersModule::class,
        DatabaseModule::class,
        NetworkModule::class]
)
interface AppComponent {
    fun inject(menuFragment: MenuFragment)
    fun inject(habitEditingFragment: HabitEditingFragment)
}