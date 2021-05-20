package com.bangkit.elevate.ui.onboardingscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.elevate.R
import com.bangkit.elevate.databinding.ActivityOnBoardingScreenBinding

class OnBoardingScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_screen)
    }
}