package com.example.stopwatch.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel(
    private val timers: MutableList<TimerModel> = mutableListOf(TimerModel("Work"))
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
        timers.remove(timers[index])
        timers[index].subscribeToValue().removeObservers(owner)
        //timers.removeAt(index)
        _liveData.value = timers
    }

    fun renameTimer(index: Int, newName: String) {
        timers[index].name = newName
        _liveData.value = timers
    }
}