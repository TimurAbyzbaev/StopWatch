package com.example.stopwatch.application

import android.app.Application
import com.example.stopwatch.di.koin.application
import com.example.stopwatch.di.koin.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StopwatchApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(application, mainScreen))
        }
    }
}