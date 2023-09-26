package com.example.stopwatch.model

import com.example.stopwatch.utils.DEFAULT_TIME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchListOrchestrator(
    private val stopwatchStateHolder: StopwatchStateHolder,
    private val scope: CoroutineScope
) {
    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(DEFAULT_TIME)
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        if (job == null) startJob()
        stopwatchStateHolder.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                mutableTicker.value = stopwatchStateHolder.getStringTimeRepresentation()
                delay(10)
            }
        }
    }

    fun pause() {
        stopwatchStateHolder.pause()
        stopJob()
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    fun stop() {
        stopwatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun clearValue() {
        mutableTicker.value = DEFAULT_TIME
    }
}