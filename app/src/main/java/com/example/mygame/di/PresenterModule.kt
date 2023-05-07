package com.example.mygame.di

import com.example.mygame.ui.bonusgame.BonusGamePresenter
import com.example.mygame.ui.choosegame.ChooseGamePresenter
import com.example.mygame.ui.splash.SplashPresenter
import com.example.mygame.ui.game1.Game1Presenter
import com.example.mygame.ui.game2.Game2Presenter
import com.example.mygame.ui.settings.SettingsPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presenterModule = module {
    factory { BonusGamePresenter(get(), get(named("clickSound")), get(named("winSound"))) }
    factory { ChooseGamePresenter(get(), get(named("clickSound")) )}
    factory { Game1Presenter(get(), get(named("clickSound")), get(named("winSound"))) }
    factory { Game2Presenter(get(), get(named("clickSound")), get(named("winSound"))) }
    factory { SettingsPresenter(get(), get(named("clickSound"))) }
    factory { SplashPresenter(get()) }
}