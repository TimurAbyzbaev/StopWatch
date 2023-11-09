package com.example.stopwatch

import com.example.stopwatch.model.TimestampMillisecondsFormatter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TimestampMillisecondsFormatterTest {
    val timestampMillisecondsFormatter = TimestampMillisecondsFormatter()

    @Test
    fun format_test() {
        val hours: Long = 1
        val minutes: Long = 26
        val seconds: Long = 1
        val milliseconds: Long = 102
        val totalMilliseconds : Long =
            ((((hours * 60 + minutes) * 60) + seconds) * 1000) + milliseconds

        if (hours > 0) {
            Assert.assertEquals(
                "${hours.pad(2)}:${minutes.pad(2)}:${seconds.pad(2)}",
                timestampMillisecondsFormatter.format(totalMilliseconds))
        } else {
            Assert.assertEquals(
                "${minutes.pad(2)}:${seconds.pad(2)}:${milliseconds.pad(3)}",
                timestampMillisecondsFormatter.format(totalMilliseconds))
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')
}