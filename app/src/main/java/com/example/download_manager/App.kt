package com.example.download_manager

import android.app.Application
import com.example.download_manager.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(AppModule.appModule)
        }
    }
}