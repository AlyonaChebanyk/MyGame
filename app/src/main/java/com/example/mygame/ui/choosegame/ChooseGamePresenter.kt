package com.example.mygame.ui.choosegame

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.mygame.adapters.SlotAdapter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Named

@InjectViewState
class ChooseGamePresenter(
    private val sharedPref: SharedPreferences,
    @Named("clickSound") private val mediaPlayerOnClickButton: MediaPlayer
): MvpPresenter<ChooseGameView>() {

    private lateinit var adapter1: SlotAdapter
    private lateinit var adapter2: SlotAdapter
    private lateinit var adapter3: SlotAdapter
    private lateinit var adapter4: SlotAdapter

    var musicVolume = 0
    private set

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        musicVolume = sharedPref.getInt("musicVolume", 5)
    }

    fun playOnClickSound() {
        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat()/7, musicVolume.toFloat()/7)
        mediaPlayerOnClickButton.start()
    }

}