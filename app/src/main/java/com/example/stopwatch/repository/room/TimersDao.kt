package com.example.stopwatch.repository.room

import androidx.room.*


@Dao
interface TimersDao {
    //получить список таймеров
    @Query("SELECT * FROM TimersEntity")
    suspend fun all(): List<TimersEntity>

    //получить конкретный таймер
    @Query("SELECT * FROM TimersEntity WHERE timerId LIKE :timerId")
    suspend fun getTimerById(timerId: Int): TimersEntity?

    //Сохранить новый таймер
    //OnConflict = OnConflictStrategy.IGNORE означает, что дубликаты не будут сохраняться
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: TimersEntity)

    // Вставить список таймеров
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<TimersEntity>)

    // Обновить таймер
    @Update
    suspend fun update(entity: TimersEntity)

    // Удалить таймер
    @Delete
    suspend fun delete(entity: TimersEntity)
}