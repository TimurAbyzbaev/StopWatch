package com.example.stopwatch.repository

import com.example.stopwatch.viewmodel.TimerModel

interface Repository<T> {
    suspend fun getTimer(timerModel: TimerModel): T
}