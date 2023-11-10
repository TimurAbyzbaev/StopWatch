package com.example.stopwatch.repository.database

import com.example.stopwatch.viewmodel.TimerModel

interface DataSource<T> {
    suspend fun getTimer(timerName: String): TimerModel
    suspend fun getAllTimers() : List<TimerModel>
    suspend fun saveAllToDB(timers: List<TimerModel>)
    suspend fun updateTimer(timer: TimerModel)
}