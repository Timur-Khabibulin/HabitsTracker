package com.timurkhabibulin.myhabits.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM Habits")
    fun getAll(): Flow<List<HabitDbEntity>>

    @Insert
    suspend fun insert(habit: HabitDbEntity): Long

    @Update
    suspend fun update(habit: HabitDbEntity)

    @Query("SELECT * FROM Habits WHERE id LIKE :id")
    fun findById(id: Long): Flow<HabitDbEntity>
}