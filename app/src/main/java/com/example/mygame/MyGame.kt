package com.example.mygame

import android.app.Application
import com.example.mygame.di.presenterModule
import com.example.mygame.di.sharedPreferencesModule
import com.example.mygame.di.soundModule
import timber.log.Timber
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyGame : Application() {
    override fun onCreate() {

        startKoin {
            androidContext(this@MyGame)
            modules(
                listOf(
                    sharedPreferencesModule,
                    soundModule,
                    presenterModule
                )
            )
        }

        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}