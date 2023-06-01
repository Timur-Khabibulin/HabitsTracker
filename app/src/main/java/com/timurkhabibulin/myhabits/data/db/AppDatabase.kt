package com.timurkhabibulin.myhabits.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitDbEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

/*    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Habits"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }*/
}