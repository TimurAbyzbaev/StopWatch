package com.example.stopwatch.viewmodel

import androidx.lifecycle.LiveData
import com.example.stopwatch.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TimerModel(
    var id: Int,
    var name: String = "Timer",
    var value: Long = 0L
) : BaseViewModel() {
    init {
        val timestampMillisecondsFormatter = TimestampMillisecondsFormatter()
        _mutableLiveData.postValue(timestampMillisecondsFormatter.format(value))
    }
    private val liveDataForViewToObserve: LiveData<String> = _mutableLiveData
    var started = false
        private set


    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        stopwatchStateHolder = StopwatchStateHolder(
            StopwatchStateCalculator(timestampProvider, ElapsedTimeCalculator(timestampProvider)),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter(),
            value
        ),
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    fun subscribeToValue(): LiveData<String> {
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
        started = true
    }

    fun pauseClicked() {
        stopwatchListOrchestrator.pause()
        started = false
    }

    fun stopClicked() {
        stopwatchListOrchestrator.stop()
        started = false
    }
}