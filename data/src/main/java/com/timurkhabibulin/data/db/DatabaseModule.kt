package com.timurkhabibulin.data.db

import android.content.Context
import androidx.room.Room
import com.timurkhabibulin.domain.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    fun provideHabitsRepository(appDatabase: AppDatabase): HabitsRepository {
        return HabitsRepositoryImpl(appDatabase.habitDao())
    }

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "Habits"
        ).build()
    }
}