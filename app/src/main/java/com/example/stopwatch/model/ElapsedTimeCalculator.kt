package com.example.stopwatch.model

class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider
) {
    fun calculate(state: StopwatchState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else 0
        return timePassedSinceStart + state.elapsedTime
    }
}