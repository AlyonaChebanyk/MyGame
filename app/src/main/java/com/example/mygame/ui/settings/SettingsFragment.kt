package com.example.mygame.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygame.R
import com.example.mygame.adapters.VolumeAdapter
import com.example.mygame.databinding.FragmentSettingsBinding
import com.example.mygame.ui.MainActivity
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get


class SettingsFragment : MvpAppCompatFragment(), SettingsView {

    private lateinit var binding: FragmentSettingsBinding

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    @ProvidePresenter
    fun provideSettingsPresenter() = get<SettingsPresenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            backButton.setOnClickListener {
                presenter.playOnClickSound()
                if (presenter.musicVolume == 0) {
                   (activity as MainActivity).vibrate(context)
                }
                findNavController().navigate(R.id.action_settingsFragment_to_chooseGameFragment)
            }
        }

    }

    override fun vibrate() {
        (activity as MainActivity).vibrate(context)
    }

    override fun setMusicAdapter(musicAdapter: VolumeAdapter) {
        binding.musicRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = musicAdapter
        }
    }

    override fun setVibrationAdapter(vibrationAdapter: VolumeAdapter) {
        binding.vibrationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = vibrationAdapter
        }
    }

}

interface VolumeListener {
    fun onSetVibration(vibration: Int)
    fun onSetMusic(music: Int)
}