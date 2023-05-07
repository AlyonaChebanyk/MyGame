package com.example.mygame.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mygame.R
import com.example.mygame.databinding.FragmentSplashBinding
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get

class SplashFragment : MvpAppCompatFragment(), SplashView{

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    @ProvidePresenter
    fun provideSplashPresenter() = get<SplashPresenter>()

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun gotToWebView(link: String) {
        val bundle = bundleOf("link" to link)
        findNavController().navigate(
            R.id.action_splashFragment_to_webViewFragment,
            bundle
        )
    }

    override fun goToGame() {
        findNavController().navigate(R.id.action_splashFragment_to_chooseGameFragment)
    }
}