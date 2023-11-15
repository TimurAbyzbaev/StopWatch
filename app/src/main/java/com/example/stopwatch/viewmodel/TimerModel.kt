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
    private val elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider)

    private val stopwatchStateHolder = StopwatchStateHolder(
        StopwatchStateCalculator(timestampProvider, ElapsedTimeCalculator(timestampProvider)),
        elapsedTimeCalculator,
        TimestampMillisecondsFormatter(),
        value)

        private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        stopwatchStateHolder = stopwatchStateHolder,
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    fun getElapsedTime() : Long {
        return if(started) {
            try {
                elapsedTimeCalculator.calculate(stopwatchStateHolder.currentState as StopwatchState.Running)
            } catch (e: java.lang.ClassCastException) {
                value
            } finally {
                value
            }

        } else {
            value
        }
    }


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