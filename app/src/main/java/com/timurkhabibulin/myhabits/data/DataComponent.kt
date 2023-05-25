package com.timurkhabibulin.myhabits.data

import com.timurkhabibulin.myhabits.data.db.DatabaseModule
import com.timurkhabibulin.myhabits.data.network.NetworkModule
import com.timurkhabibulin.myhabits.presentation.ContextModule
import com.timurkhabibulin.myhabits.presentation.DispatchersModule
import com.timurkhabibulin.myhabits.presentation.view.fragments.HabitEditingFragment
import com.timurkhabibulin.myhabits.presentation.view.fragments.MenuFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class, ContextModule::class, DispatchersModule::class])
interface DataComponent {
    //Todo: Здесь мы ничего не знаем о presentation
    fun inject(menuFragment: MenuFragment)

    fun inject(habitEditingFragment: HabitEditingFragment)
}