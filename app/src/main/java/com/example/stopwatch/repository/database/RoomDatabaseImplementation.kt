package com.example.stopwatch.repository.database

import com.example.stopwatch.repository.room.TimersDao
import com.example.stopwatch.repository.room.TimersEntity
import com.example.stopwatch.viewmodel.TimerModel

class RoomDatabaseImplementation(private val timersDao: TimersDao) :
    DataSource<List<TimerModel>> {

    override suspend fun getTimer(timerName: String) : TimerModel {
        val timerEntity: TimersEntity = timersDao.getDataByName(timerName)
        return convertTimerEntityToTimerModel(timerEntity)
    }

    override suspend fun getAllTimers(): List<TimerModel> {
        val timerEntityList = timersDao.all()
        return convertTimersEntityListToTimerModelList(timerEntityList)
    }

    override suspend fun saveAllToDB(timers: List<TimerModel>) {
        val timersEntityList = convertTimerModelListToTimerEntityList(timers)
        timersDao.insertAll(timersEntityList)
    }

    override suspend fun updateTimer(timer: TimerModel) {

        timersDao.update(TimersEntity(timer.name, 0L))
    }

    private fun convertTimerModelListToTimerEntityList(timers: List<TimerModel>): List<TimersEntity> {
        val timerEntityList = mutableListOf<TimersEntity>()
        for(timer in timers) {
            timerEntityList.add(TimersEntity(timer.name, 0L))
        }
        return timerEntityList
    }

    private fun convertTimerEntityToTimerModel(timersEntity: TimersEntity) : TimerModel {
        return TimerModel(timersEntity.timerName)
    }

    private fun convertTimersEntityListToTimerModelList(timerEntityList: List<TimersEntity>) : List<TimerModel> {
        val timerModelList = mutableListOf<TimerModel>()
        for(timerEntity in timerEntityList) {
            timerModelList.add(TimerModel(timerEntity.timerName))
        }
        return timerModelList
    }

    private fun convertTimerModelToTimerEntity(timer: TimerModel) : TimersEntity {
        return TimersEntity(timer.name, timer.value)
    }

}