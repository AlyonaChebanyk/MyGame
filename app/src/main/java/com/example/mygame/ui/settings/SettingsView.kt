package com.example.mygame.ui.settings

import com.example.mygame.adapters.VolumeAdapter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface SettingsView: MvpView {
    fun vibrate()
    fun setMusicAdapter(musicAdapter: VolumeAdapter)
    fun setVibrationAdapter(vibrationAdapter: VolumeAdapter)
}