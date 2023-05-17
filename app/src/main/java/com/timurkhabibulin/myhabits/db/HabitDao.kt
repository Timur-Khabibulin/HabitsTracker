package com.timurkhabibulin.myhabits.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface HabitDao {

    @Query("SELECT * FROM Habits")
    fun getAll(): Flow<List<HabitDbEntity>>

    @Insert
    suspend fun insert(habit: HabitDbEntity): Long

    @Update
    suspend fun update(habit: HabitDbEntity)

    @Query("SELECT * FROM Habits WHERE id LIKE :id")
    fun findById(id: Long): LiveData<HabitDbEntity>
}