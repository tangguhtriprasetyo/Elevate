package com.bangkit.elevate.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.data.firebase.UserRepository

class LoginViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository()

    fun signInWithGoogle(idToken: String): LiveData<UserEntity> =
        userRepository.signInWithGoogle(idToken)
}