package com.example.mygame.ui.choosegame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.mygame.R
import com.example.mygame.adapters.Orientation
import com.example.mygame.adapters.OrientationListener
import com.example.mygame.databinding.FragmentChooseGameBinding
import com.example.mygame.di.presenterModule
import com.example.mygame.ui.MainActivity
import com.example.mygame.ui.bonusgame.BonusGamePresenter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get
import timber.log.Timber

class ChooseGameFragment : MvpAppCompatFragment(), ChooseGameView{

    private lateinit var binding: FragmentChooseGameBinding

    @InjectPresenter
    lateinit var presenter: ChooseGamePresenter

    @ProvidePresenter
    fun provideChooseGamePresenter() = get<ChooseGamePresenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = FragmentChooseGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            settingsButton.setOnClickListener {
               (activity as MainActivity).vibrate(context)
                findNavController().navigate(R.id.action_chooseGameFragment_to_settingsFragment)
            }
            docButton.setOnClickListener {
               (activity as MainActivity).vibrate(context)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(browserIntent)
            }
            game1CardView.setOnClickListener {
                presenter.playOnClickSound()
                if (presenter.musicVolume == 0) {
                   (activity as MainActivity).vibrate(context)
                }
                findNavController().navigate(R.id.action_chooseGameFragment_to_game1Fragment)
            }
            game2CardView.setOnClickListener {
                presenter.playOnClickSound()
                if (presenter.musicVolume == 0) {
                   (activity as MainActivity).vibrate(context)
                }
                findNavController().navigate(R.id.action_chooseGameFragment_to_game2Fragment)
            }
            bonusGameCardView.setOnClickListener {
                presenter.playOnClickSound()
                if (presenter.musicVolume == 0) {
                   (activity as MainActivity).vibrate(context)
                }
                findNavController().navigate(R.id.action_chooseGameFragment_to_bonusGameFragment)
            }
        }
    }
}