package com.bangkit.elevate.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.data.firebase.FirebaseServices

class LoginViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun signInWithGoogle(idToken: String): LiveData<UserEntity> =
        firebaseServices.signInWithGoogle(idToken)

    fun createdNewUser(authUser: UserEntity): LiveData<UserEntity> =
        firebaseServices.createUserToFirestore(authUser)
}