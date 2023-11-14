package com.example.stopwatch.repository

import com.example.stopwatch.viewmodel.TimerModel

interface Repository<T> {
    suspend fun getTimer(timerName: String): T
    suspend fun saveToDB(timer: TimerModel)

    suspend fun getAllTimers() : List<T>
    suspend fun updateTimerInDB(timer: TimerModel)
    suspend fun deleteTimerFromDB(timer: TimerModel)
}