package com.example.mygame.ui.splash

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface SplashView: MvpView {
    fun gotToWebView(link: String)
    fun goToGame()
}