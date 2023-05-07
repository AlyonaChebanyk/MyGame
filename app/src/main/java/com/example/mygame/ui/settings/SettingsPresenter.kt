package com.example.mygame.ui.settings

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.mygame.adapters.Volume
import com.example.mygame.adapters.VolumeAdapter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Named

@InjectViewState
class SettingsPresenter(
    private val sharedPref: SharedPreferences,
    @Named("clickSound") private val mediaPlayerOnClickButton: MediaPlayer
): MvpPresenter<SettingsView>(), VolumeListener {

    private lateinit var musicAdapter: VolumeAdapter
    private lateinit var vibrationAdapter: VolumeAdapter

    var musicVolume: Int = 0
    private set

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val vibrationVolume = sharedPref.getInt("vibrationVolume", 5)
        vibrationAdapter = VolumeAdapter(vibrationVolume, this, Volume.Vibration)

        musicVolume = sharedPref.getInt("musicVolume", 5)
        musicAdapter = VolumeAdapter(musicVolume, this, Volume.Music)

        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat()/7, musicVolume.toFloat()/7)

        viewState.setVibrationAdapter(vibrationAdapter)
        viewState.setMusicAdapter(musicAdapter)

    }

    override fun onSetVibration(vibration: Int) {
        musicVolume = sharedPref.getInt("musicVolume", 5)
        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat()/7, musicVolume.toFloat()/7)
        playOnClickSound()
        if (musicVolume == 0) {
            viewState.vibrate()
        }

        sharedPref.edit()?.putInt("vibrationVolume", vibration)?.apply()
        vibrationAdapter.volume = vibration
        vibrationAdapter.notifyDataSetChanged()
    }

    override fun onSetMusic(music: Int) {
        musicVolume = music
        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat()/7, musicVolume.toFloat()/7)
        playOnClickSound()
        if (musicVolume == 0) {
            viewState.vibrate()
        }
        sharedPref.edit()?.putInt("musicVolume", music)?.apply()
        musicAdapter.volume = music
        musicAdapter.notifyDataSetChanged()
    }

    fun playOnClickSound() {
        mediaPlayerOnClickButton.setVolume(musicVolume.toFloat() / 7, musicVolume.toFloat() / 7)
        mediaPlayerOnClickButton.start()
    }

}