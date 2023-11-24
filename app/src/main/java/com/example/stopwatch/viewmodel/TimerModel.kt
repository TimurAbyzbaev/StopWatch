package com.example.stopwatch.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.stopwatch.di.koin.application
import com.example.stopwatch.model.*
import com.example.stopwatch.view.CurrentTimerNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TimerModel(
    var id: Int,
    var name: String = "Timer",
    var value: Long = 0L
) : BaseViewModel() {
    init {
        val timestampMillisecondsFormatter = TimestampMillisecondsFormatter()
        _mutableLiveData.postValue(timestampMillisecondsFormatter.format(value))
    }
    private val notification by inject<CurrentTimerNotification>(CurrentTimerNotification::class.java)

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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun startClicked() {
        stopwatchListOrchestrator.start()
        started = true
        notification.setCurrentTimer(this)
        notification.createNotification()
    }

    fun pauseClicked() {
        stopwatchListOrchestrator.pause()
        started = false
        notification.notificationActive = false
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun stopClicked() {
        stopwatchListOrchestrator.stop()
        started = false
        notification.cancelNotification()
        notification.notificationActive = false
    }
}