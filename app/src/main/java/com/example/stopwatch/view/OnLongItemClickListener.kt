package com.example.stopwatch.view

import com.example.stopwatch.viewmodel.TimerModel

interface OnLongItemClickListener {
    fun onItemClick(viewModel: TimerModel)
}