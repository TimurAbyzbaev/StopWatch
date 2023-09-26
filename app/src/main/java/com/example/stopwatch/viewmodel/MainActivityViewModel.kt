package com.example.stopwatch.viewmodel

import androidx.lifecycle.LiveData
import com.example.stopwatch.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivityViewModel : BaseViewModel() {
    private val liveDataForViewToObserve: LiveData<String> = _mutableLiveData

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        stopwatchStateHolder = StopwatchStateHolder(
            StopwatchStateCalculator(timestampProvider, ElapsedTimeCalculator(timestampProvider)),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    fun subscribe(): LiveData<String> {
        return liveDataForViewToObserve
    }

    fun startCollect() {
        CoroutineScope(Dispatchers.Main).launch {
            stopwatchListOrchestrator.ticker.collect {
                _mutableLiveData.value = it
            }
        }
    }

    fun startClicked() {
        stopwatchListOrchestrator.start()
    }

    fun pauseClicked() {
        stopwatchListOrchestrator.pause()
    }

    fun stopClicked() {
        stopwatchListOrchestrator.stop()
    }
}