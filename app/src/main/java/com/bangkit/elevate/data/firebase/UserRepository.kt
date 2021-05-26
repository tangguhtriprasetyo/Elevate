package com.bangkit.elevate.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.elevate.data.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithGoogle(idToken: String): LiveData<UserEntity> {
        val authenticatedUser = MutableLiveData<UserEntity>()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        CoroutineScope(IO).launch {
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isNewUser = task.result?.additionalUserInfo?.isNewUser
                        val user: FirebaseUser? = firebaseAuth.currentUser
                        if (user != null) {
                            val uid = user.uid
                            val name = user.displayName
                            val email = user.email
                            val userInfo = UserEntity(
                                uid,
                                name,
                                email
                            )
                            userInfo.isNew = isNewUser
                            authenticatedUser.postValue(userInfo)
                        }
                    } else {
                        Log.d("Error Authentication", "signInWithGoogle: ", task.exception)
                    }
                }
        }
        return authenticatedUser
    }
}