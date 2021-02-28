package com.example.mylibrary.library

import android.app.Application
import com.example.mylibrary.library.moduels.QrLibraryModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QrLibraryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(applicationContext)
            // modules
            modules(QrLibraryModules().createModules(applicationContext))
        }
    }

}