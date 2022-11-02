package io.androntainer.application.abstract

import android.app.Application

abstract class AndrontainerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSdk()
    }

    abstract fun initSdk()
}