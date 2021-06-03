package com.bangkit.elevate.ui.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.elevate.R
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.ActivityLoginBinding
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.ExperimentalCoroutinesApi

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(LoginViewModel::class.java)
        initGoogleSignInClient()


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                        loginViewModel.signInWithGoogle(account.idToken!!)
                            .observe(this, { userData ->
                                if (userData != null) {
                                    if (userData.isNew == true) {
                                        createNewUser(userData)
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Welcome back, ${userData.username}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.d("oldUser", userData.username.toString())
                                        gotoMainActivity()
                                    }
                                }
                            })
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        showLoading(false)
                        Toast.makeText(this, "Login Failed, $e", Toast.LENGTH_LONG).show()
                        Log.w(TAG, "Google sign in failed", e)
                    }
                }
            }

        binding.btnGoogleSignin.setOnClickListener {
            showLoading(true)
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
    }

    private fun createNewUser(userData: UserEntity) {
        Log.d("createdNewUser", userData.username.toString())
        loginViewModel.createdNewUser(userData).observe(this, { newUser ->
            if (newUser.isCreated == true) {
                Toast.makeText(
                    this,
                    "Hello ${userData.username}, Your Account Successfully Created!",
                    Toast.LENGTH_SHORT
                ).show()
                gotoMainActivity()
            }
        })
    }

    @ExperimentalCoroutinesApi
    private fun gotoMainActivity() {
        showLoading(false)
        val intent =
            Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarLogin.visibility = View.VISIBLE
        } else {
            binding.progressBarLogin.visibility = View.GONE
        }
    }

}