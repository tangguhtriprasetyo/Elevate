package com.bangkit.elevate.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.elevate.R
import com.bangkit.elevate.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}