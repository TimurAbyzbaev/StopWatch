package com.example.stopwatch.repository.database

import com.example.stopwatch.repository.room.TimersDao
import com.example.stopwatch.repository.room.TimersEntity
import com.example.stopwatch.viewmodel.TimerModel

class RoomDatabaseImplementation(private val timersDao: TimersDao) :
    DataSource<List<TimerModel>> {

    override suspend fun getTimer(timerId: Int) : TimerModel? {
        val timerEntity: TimersEntity? = timersDao.getTimerById(timerId)
        if (timerEntity != null) {
            return convertTimerEntityToTimerModel(timerEntity)
        } else return null
    }

    override suspend fun getAllTimers(): List<TimerModel> {
        val timerEntityList = timersDao.all()
        return convertTimersEntityListToTimerModelList(timerEntityList)
    }

    override suspend fun saveAllToDB(timers: List<TimerModel>) {
        val timersEntityList = convertTimerModelListToTimerEntityList(timers)
        timersDao.insertAll(timersEntityList)
    }

    override suspend fun saveToDB(timer: TimerModel) {
        timersDao.insert(convertTimerModelToTimerEntity(timer))
    }

    override suspend fun updateTimer(timer: TimerModel) {
        timersDao.update(convertTimerModelToTimerEntity(timer))
    }

    override suspend fun deleteTimer(timer: TimerModel) {
        timersDao.delete(convertTimerModelToTimerEntity(timer))
    }

    private fun convertTimerModelListToTimerEntityList(timers: List<TimerModel>): List<TimersEntity> {
        val timerEntityList = mutableListOf<TimersEntity>()
        for(timer in timers) {
            timerEntityList.add(convertTimerModelToTimerEntity(timer))
        }
        return timerEntityList
    }

    private fun convertTimerEntityToTimerModel(timersEntity: TimersEntity) : TimerModel {
        return TimerModel(timersEntity.id, timersEntity.timerName, timersEntity.timerValue)
    }

    private fun convertTimersEntityListToTimerModelList(timerEntityList: List<TimersEntity>) : List<TimerModel> {
        val timerModelList = mutableListOf<TimerModel>()
        for(timerEntity in timerEntityList) {
            timerModelList.add(TimerModel(timerEntity.id, timerEntity.timerName, timerEntity.timerValue))
        }
        return timerModelList
    }

    private fun convertTimerModelToTimerEntity(timer: TimerModel) : TimersEntity {
        return TimersEntity(timer.id, timer.name, timer.value)
    }

}