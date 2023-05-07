package com.example.mygame.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.viewbinding.library.fragment.viewBinding
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.mygame.R
import com.example.mygame.databinding.FragmentWebViewBinding
import timber.log.Timber

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private val binding: FragmentWebViewBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val link = arguments?.getString("link", "https://example.com")
        with(binding){
            webView.loadUrl(link!!)
            webView.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && webView.canGoBack()) {
                    webView.goBack()
                    true
                } else {
                    false
                }
            }
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    Timber.d("Page finished: $url")
                    // Handle page loading finished event
                }
            }
        }

    }
}