package com.example.mygame.ui.splash

import android.content.SharedPreferences
import com.example.mygame.ui.splash.SplashView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import timber.log.Timber

@InjectViewState
class SplashPresenter(private val sharedPref: SharedPreferences) : MvpPresenter<SplashView>() {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var link: String
    private var status: Boolean = true

    private val uiScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        ioScope.launch {
            delay(2000)
            uiScope.launch {
                link = sharedPref.getString("link", "").toString()
                if (link.isEmpty()) {
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val updated = task.result
                                Timber.d("Config params updated: $updated")
                            }
                            link = remoteConfig.getString("link")
                            status = remoteConfig.getBoolean("status")
                            if (status) {
                                sharedPref.edit().putString("link", link).apply()
                                viewState.gotToWebView(link)
                            } else {
                                viewState.goToGame()
                            }
                        }
                } else {
                    viewState.gotToWebView(link)
                }
            }
        }
    }
}