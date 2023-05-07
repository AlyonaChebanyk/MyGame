package com.example.mygame.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single { provideSharedPreferences(androidApplication()) }
}

fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)