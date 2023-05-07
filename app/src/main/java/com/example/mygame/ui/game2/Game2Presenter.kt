package com.example.mygame.ui.game2

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.mygame.adapters.SlotItem
import com.example.mygame.ui.game1.Game1View
import moxy.InjectViewState
import moxy.MvpPresenter
import timber.log.Timber
import javax.inject.Named

@InjectViewState
class Game2Presenter(
    private val sharedPref: SharedPreferences,
    @Named("clickSound") private val mediaPlayerOnClickButton: MediaPlayer,
    @Named("winSound") private val mediaPlayerOnWin: MediaPlayer
) : MvpPresenter<Game2View>() {

    var bid = 0
    var win = 0
        private set

    var balance = 0

    var musicVolume = 0
        private set

    override fun attachView(view: Game2View?) {
        super.attachView(view)
        viewState.displayBid(bid)
        viewState.displayWin(win)
        balance = sharedPref.getInt("balance", 1000)
        viewState.displayBalance(balance)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        musicVolume = sharedPref.getInt("musicVolume", 5)
        balance = sharedPref.getInt("balance", 1000)
        viewState.displayBalance(balance)
        viewState.setAdapters()
        viewState.setOnScrollListenerOnAdapters()
        viewState.disableOnTouchRecyclerView()
        viewState.displayBid(bid)
        viewState.displayWin(win)
    }

    fun playOnClickSound() {
        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat() / 7, musicVolume.toFloat() / 7)
        mediaPlayerOnClickButton.start()
    }

    private fun playOnWin() {
        mediaPlayerOnWin.setVolume(musicVolume.toFloat() / 7, musicVolume.toFloat() / 7)
        mediaPlayerOnWin.start()
    }

    fun addBid() {
        bid += 10
    }

    fun minusBid() {
        bid -= 10
    }

    fun minusBidFromBalance() {
        balance -= bid
        if (balance <= 0) {
            balance = 1000
        }
        sharedPref.edit().putInt("balance", balance).apply()
    }

    fun calculateWin(currentGameItems: MutableList<SlotItem>) {

        val currentGameItemsSet = currentGameItems.distinctBy { it.id }

        val dublicates = currentGameItems.size - currentGameItemsSet.size

        var coef = 0

        when (dublicates) {
            1 -> coef = 2
            2 -> coef = 4
            3 -> coef = 10
        }

        win = bid * coef

        if (win != 0) {
            playOnWin()
        }

        balance += win

        sharedPref.edit().putInt("balance", balance).apply()

        viewState.displayWin(win)
        viewState.displayBalance(balance)

    }

}