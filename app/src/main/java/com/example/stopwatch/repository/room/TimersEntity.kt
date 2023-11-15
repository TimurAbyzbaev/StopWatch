package com.example.stopwatch.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = arrayOf("timerId"), unique = true)))
class TimersEntity (
    @field:PrimaryKey
    @field:ColumnInfo(name = "timerId")
    var id: Int,

    @field:ColumnInfo(name = "timerName")
    var timerName: String,

    @field:ColumnInfo(name = "timerValue")
    var timerValue: Long
)
