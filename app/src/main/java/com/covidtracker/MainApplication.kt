package com.covidtracker

import android.app.Application
import com.covidtracker.di.mainViewModelModule
import com.covidtracker.di.restServiceModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(applicationContext)
            modules(restServiceModule, mainViewModelModule)
        }
    }
}