package com.timurkhabibulin.myhabits.presentation


import com.timurkhabibulin.myhabits.presentation.view.fragments.HabitEditingFragment
import com.timurkhabibulin.myhabits.presentation.view.fragments.MenuFragment
import dagger.Component
import javax.inject.Singleton
import com.timurkhabibulin.data.db.DatabaseModule
import com.timurkhabibulin.data.network.NetworkModule

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