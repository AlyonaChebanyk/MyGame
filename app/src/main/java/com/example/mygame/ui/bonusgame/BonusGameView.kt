package com.example.mygame.ui.bonusgame

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface BonusGameView: MvpView {
    fun displayBalance(balance: Int)
    fun setCardCameraDistance()
    fun setOnClickListenerOnBackButton()
    fun setOnClickListenerOnCards()
    fun createAnimators()
}