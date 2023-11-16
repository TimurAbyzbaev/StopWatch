package com.example.stopwatch.repository

import com.example.stopwatch.repository.database.DataSource
import com.example.stopwatch.viewmodel.TimerModel

class RepositoryImplementation(private  val dataSource: DataSource<List<TimerModel>>) :
    Repository<TimerModel> {
    override suspend fun getTimer(timerId: Int): TimerModel? {
        return dataSource.getTimer(timerId)
    }

    override suspend fun saveToDB(timer: TimerModel) {
        dataSource.saveToDB(timer)
    }

    override suspend fun getAllTimers(): List<TimerModel> {
        return dataSource.getAllTimers()
    }

    override suspend fun updateTimerInDB(timer: TimerModel) {
        dataSource.updateTimer(timer)
    }

    override suspend fun deleteTimerFromDB(timer: TimerModel) {
        dataSource.deleteTimer(timer)
    }

}