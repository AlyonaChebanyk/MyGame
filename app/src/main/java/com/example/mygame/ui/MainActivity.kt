package com.example.mygame.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mygame.R


class MainActivity : AppCompatActivity(){

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    fun vibrate(context: Context?) {
        val vibrationVolume = sharedPref.getInt("vibrationVolume", 5)
        // Get the Vibrator system service
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if (vibrationVolume != 0){
            // Check if device has vibrator and vibrator service is available
            if (vibrator != null && vibrator.hasVibrator()) {
                // Vibrate for the specified duration
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            150 * vibrationVolume.toLong() / 7,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(
                        150 * vibrationVolume.toLong() / 7
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
    }
}