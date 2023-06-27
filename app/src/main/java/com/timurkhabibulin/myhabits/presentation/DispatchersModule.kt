package com.timurkhabibulin.myhabits.presentation

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class DispatchersModule {
    @Provides
    fun provideDispatcher() = Dispatchers.IO
}



