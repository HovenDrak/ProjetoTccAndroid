package com.example.smarthhome.application

import android.app.Application
import com.example.smarthhome.di.modules.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AppAplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppAplication)
            modules(appModules)
        }
    }
}