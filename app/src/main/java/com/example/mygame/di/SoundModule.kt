package com.example.mygame.di

import android.content.Context
import android.media.MediaPlayer
import com.example.mygame.R
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val soundModule = module {
    single(named("clickSound")) { provideMediaPlayer(androidApplication(), R.raw.button_click) }
    single(named("winSound")) { provideMediaPlayer(androidApplication(), R.raw.instant_win) }
}

fun provideMediaPlayer(context: Context, resourceId: Int): MediaPlayer =
    MediaPlayer.create(context, resourceId)

