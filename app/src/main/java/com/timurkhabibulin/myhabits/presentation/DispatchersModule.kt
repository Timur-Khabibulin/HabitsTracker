package com.timurkhabibulin.myhabits.presentation

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class DispatchersModule(private val dispatcher: CoroutineDispatcher) {

    @Provides
    fun provideDispatcher() = dispatcher
}
