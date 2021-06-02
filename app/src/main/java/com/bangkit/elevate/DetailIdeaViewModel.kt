package com.bangkit.elevate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.firebase.FirebaseServices

class DetailIdeaViewModel: ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getIdeaData(uid: String): LiveData<IdeaEntity> =
        firebaseServices.getIdeaData(uid)


}