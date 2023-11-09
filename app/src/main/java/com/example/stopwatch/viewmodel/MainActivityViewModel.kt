package com.example.stopwatch.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel(
    private val timers: MutableList<TimerModel> = mutableListOf(
        TimerModel("Work"),
        TimerModel("Coffee"),
        TimerModel("Smoke"),
        TimerModel("Programming"))
) : ViewModel() {

    private val _liveData = MutableLiveData<MutableList<TimerModel>>()
    private val liveData: LiveData<MutableList<TimerModel>> = _liveData

    fun subscribeToLiveData(): LiveData<MutableList<TimerModel>> {
        _liveData.value = timers
        return liveData
    }

    fun addTimer(name: String = "New Timer") {
        timers.add(TimerModel(name))
        _liveData.value = timers
    }

    fun removeTimer(index: Int, owner: LifecycleOwner) {
        timers[index].stopClicked()
        timers[index].subscribeToValue().removeObservers(owner)
        timers.remove(timers[index])
        _liveData.value = timers
    }

    fun renameTimer(index: Int, newName: String) {
        timers[index].name = newName
        _liveData.value = timers
    }
}