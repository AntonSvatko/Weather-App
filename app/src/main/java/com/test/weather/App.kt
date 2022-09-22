package com.test.weather

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        App.applicationContext = applicationContext
    }

    companion object {
        var applicationContext: Context? = null
    }
}