package com.example.stopwatch.di.koin

import androidx.room.Room
import com.example.stopwatch.repository.Repository
import com.example.stopwatch.repository.RepositoryImplementation
import com.example.stopwatch.repository.database.RoomDatabaseImplementation
import com.example.stopwatch.repository.room.TimersDataBase
import com.example.stopwatch.view.MainActivity
import com.example.stopwatch.viewmodel.MainActivityViewModel
import com.example.stopwatch.viewmodel.TimerModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), TimersDataBase::class.java, "TimersDB").build() }
    single { get<TimersDataBase>().timersDao() }

    single<Repository<TimerModel>> {
        RepositoryImplementation(RoomDatabaseImplementation(get()))
    }
}

val mainScreen = module {
    scope(named<MainActivity>()) {
        viewModel { MainActivityViewModel(get()) }
    }
}