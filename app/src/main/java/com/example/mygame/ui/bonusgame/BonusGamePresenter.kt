package com.example.mygame.ui.bonusgame

import android.content.SharedPreferences
import android.media.MediaPlayer
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Named

@InjectViewState
class BonusGamePresenter(
    private val sharedPref: SharedPreferences,
    @Named("clickSound") private val mediaPlayerOnClickButton: MediaPlayer,
    @Named("winSound") private val mediaPlayerOnWin: MediaPlayer
) : MvpPresenter<BonusGameView>() {

    var musicVolume = 0
    private set

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        musicVolume = sharedPref.getInt("musicVolume", 5)
        viewState.createAnimators()
        viewState.displayBalance(getBalance())
        viewState.setOnClickListenerOnBackButton()
        viewState.setCardCameraDistance()
        viewState.setOnClickListenerOnCards()
    }

    fun getBalance(): Int{
        return sharedPref.getInt("balance", 1000)
    }

    fun setBalance(balance: Int){
        sharedPref.edit().putInt("balance", balance).apply()
    }

    fun playOnClickSound() {
        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat() / 7, musicVolume.toFloat() / 7)
        mediaPlayerOnClickButton.start()
    }

    fun playOnWin() {
        mediaPlayerOnWin.setVolume(musicVolume.toFloat() / 7, musicVolume.toFloat() / 7)
        mediaPlayerOnWin.start()
    }
}