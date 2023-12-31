package com.example.stopwatch.viewmodel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.example.stopwatch.R
import com.example.stopwatch.application.StopwatchApp
import com.example.stopwatch.di.koin.application
import com.example.stopwatch.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

//TimerModel("Work"),
//TimerModel("Coffee"),
//TimerModel("Smoke"),
//TimerModel("Programming")


class MainActivityViewModel(
    private val repository: Repository<TimerModel>
) : ViewModel() {
    var startSave = false

    fun startSave(){
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if(startSave){
                    val timersCopy = timers.toList()
                    for (timer in timersCopy) {
                        if (repository.getTimer(timer.id)?.value != timer.getElapsedTime()) {
                            timer.value = timer.getElapsedTime()
                            updateTimerInDB(timers.indexOf(timer))
                        }
                    }
                    startSave()
                }
            }
        }
    }

    private val timers: MutableList<TimerModel> = mutableListOf()
    private suspend fun getTimersFromDB() {
        val repositoryTimers = repository.getAllTimers()
        if(repositoryTimers.isNotEmpty()) {
            for(timer in repositoryTimers) {
                timers.add(timer)
            }
        } else {
            addTimer("New Start Timer")
        }
    }
    private val _liveData = MutableLiveData<MutableList<TimerModel>>()
    private val liveData: LiveData<MutableList<TimerModel>> = _liveData

    fun subscribeToLiveData(): LiveData<MutableList<TimerModel>> {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getTimersFromDB()
            }
        }
        _liveData.postValue(timers)
        return liveData
    }

    fun addTimer(name: String = "New Timer") {
        val id = Random.nextInt()
        val newTimer = TimerModel(id, name)
        timers.add(newTimer)
        viewModelScope.launch { repository.saveToDB(newTimer) }
        _liveData.postValue(timers)
    }

    fun removeTimer(index: Int, owner: LifecycleOwner) {
        timers[index].stopClicked()
        timers[index].subscribeToValue().removeObservers(owner)
        viewModelScope.launch { repository.deleteTimerFromDB(timers[index]) }
        timers.remove(timers[index])
        _liveData.postValue(timers)
    }

    fun renameTimer(index: Int, newName: String) {
        timers[index].name = newName
        updateTimerInDB(index)
        _liveData.postValue(timers)
    }

    private fun updateTimerInDB(index: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if(timers.size < index){
                    repository.updateTimerInDB(timer = timers[index])
                }
            }
        }
    }
}