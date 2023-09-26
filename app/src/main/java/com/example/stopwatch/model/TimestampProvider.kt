package com.example.stopwatch.model

interface TimestampProvider {
    fun getMilliseconds(): Long
}