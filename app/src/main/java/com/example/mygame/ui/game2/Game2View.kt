package com.example.mygame.ui.game2

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface Game2View: MvpView {
    fun displayBalance(balance: Int)
    fun setAdapters()
    fun setOnScrollListenerOnAdapters()
    fun disableOnTouchRecyclerView()
    fun displayBid(bid: Int)
    fun displayWin(win: Int)
}