package com.bangkit.elevate.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.ActivitySplashScreenBinding
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.onboardingscreen.OnBoardingScreenActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private val timeOut: Long = 2000
    private lateinit var binding: ActivitySplashScreenBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var user = UserEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseUser = firebaseAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseUser != null) {
                user.isAuthenticated = true
                user.uid = firebaseUser.uid
                user.username = firebaseUser.displayName
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this@SplashScreenActivity, OnBoardingScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, timeOut)
    }
}