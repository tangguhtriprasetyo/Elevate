package com.bangkit.elevate.ui.dashboard.ideator

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.firebase.FirebaseServices

class IdeatorViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()
    private val _alreadyUploadTerm1 = MutableLiveData<Boolean>()
    val alreadyUploadTerm1: LiveData<Boolean> = _alreadyUploadTerm1

    fun uploadFiles(uri: Uri, uid: String, type: String, name: String): LiveData<String> =
        firebaseServices.uploadFiles(uri, uid, type, name)

    fun uploadImages(uri: Uri, uid: String, type: String, name: String): LiveData<String> =
        firebaseServices.uploadFiles(uri, uid, type, name)

    fun postIdeaData(ideaData: IdeaEntity, uid: String): LiveData<Boolean> =
        firebaseServices.uploadIdeaData(ideaData, uid)

    fun getIdeaData(uid: String): LiveData<IdeaEntity> =
        firebaseServices.getIdeaData(uid)

    fun getStatusTerm1(boolean: Boolean) {
        _alreadyUploadTerm1.value = boolean
    }
}