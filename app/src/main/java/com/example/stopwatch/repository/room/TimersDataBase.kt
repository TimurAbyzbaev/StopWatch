package com.example.stopwatch.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TimersEntity::class), version = 1, exportSchema = false)
abstract class TimersDataBase : RoomDatabase() {
    abstract fun timersDao() : TimersDao
}