package com.example.stopwatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel(
    private val timers: MutableList<TimerModel> = mutableListOf(TimerModel("Work"))
): ViewModel(){

    private val _liveData = MutableLiveData<MutableList<TimerModel>>()
    private val liveData: LiveData<MutableList<TimerModel>> = _liveData

    fun subscribeToLiveData(): LiveData<MutableList<TimerModel>> {
        _liveData.value = timers
        return liveData
    }

    fun addTimer(name: String = "New Timer") {
        //_liveData.value?.add(TimerModel(name))
        timers.add(TimerModel(name))
    }

    fun removeTimer(index: Int) {
        //timers.removeAt(index)
        _liveData.value?.removeAt(index)
    }

    fun renameTimer(index: Int, newName: String) {
        //timers[index].name = newName
        _liveData.value?.get(index)?.name ?: newName
    }
}