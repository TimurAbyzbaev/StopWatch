package com.example.stopwatch.model

class TimestampMillisecondsFormatter {
    fun format(timestamp: Long): String {
        val millisecondsFormatted = (timestamp % 1000).pad(3)
        val seconds = timestamp / 1000
        val secondsFormatted = (seconds % 60).pad(2)
        val minutes = seconds / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hours = minutes / 60
        val hoursFormatted = if (hours >= 100) {
            hours.pad(3)
        } else {
            hours.pad(2)
        }
        return if (hours > 0) {
            "$hoursFormatted:$minutesFormatted:$secondsFormatted"
        } else {
            "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')
}