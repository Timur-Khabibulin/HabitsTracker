package com.timurkhabibulin.myhabits.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
@TypeConverters(Converters::class)
interface HabitDao {

    @Query("SELECT * FROM Habits")
    fun getAll(): LiveData<List<HabitDbEntity>>

    @Insert
    suspend fun insert(habit: HabitDbEntity)

    @Update
    suspend fun update(habit: HabitDbEntity)

    @Query("SELECT * FROM Habits WHERE id LIKE :id")
    fun findById(id: Int): LiveData<HabitDbEntity>
}