package ru.naburnm8.queueapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import ru.naburnm8.queueapp.modules.networkModule
import ru.naburnm8.queueapp.modules.repositoryModule
import ru.naburnm8.queueapp.modules.viewmodelModule

class QueueAppApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@QueueAppApplication)
            modules(listOf(repositoryModule, viewmodelModule, networkModule))
        }
    }
}