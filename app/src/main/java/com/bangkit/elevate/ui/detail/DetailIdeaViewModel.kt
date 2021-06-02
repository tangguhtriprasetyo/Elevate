package com.bangkit.elevate.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.firebase.FirebaseServices
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DetailIdeaViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()
    private val _successfulDonate = MutableLiveData<Boolean>()
    val successfulDonate: LiveData<Boolean> = _successfulDonate

    fun getIdeaData(uid: String): LiveData<IdeaEntity> =
        firebaseServices.getIdeaData(uid)


    fun updateDonation(
        uid: String,
        ideatorId: String,
        totalDonation: Long,
        userBalance: Long
    ): LiveData<Boolean> =
        firebaseServices.updateDonation(uid, ideatorId, totalDonation, userBalance)

    fun getStatusDonation(boolean: Boolean) {
        _successfulDonate.value = boolean
    }

}