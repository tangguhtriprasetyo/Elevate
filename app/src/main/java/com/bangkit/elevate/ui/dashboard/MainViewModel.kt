package com.bangkit.elevate.ui.dashboard

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.data.firebase.FirebaseServices

class MainViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _userProfile = MutableLiveData<UserEntity>()

    fun setUserProfile(uid: String): LiveData<UserEntity> {
        _userProfile = firebaseServices.getUserProfile(uid) as MutableLiveData<UserEntity>
        return _userProfile
    }

    fun getProfileData(): LiveData<UserEntity> {
        return _userProfile
    }

    fun editProfileUser(authUser: UserEntity): LiveData<UserEntity> =
        firebaseServices.editUserProfile(authUser)

    fun uploadImages(uri: Uri, uid: String, type: String, name: String): LiveData<String> =
        firebaseServices.uploadFiles(uri, uid, type, name)
}