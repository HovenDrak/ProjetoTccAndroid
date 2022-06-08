package com.example.smarthhome.application

import android.app.Application
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.di.modules.appModules
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
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