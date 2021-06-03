package com.bangkit.elevate.ui.dashboard.funder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.firebase.FirebaseServices
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FunderProgressViewModel : ViewModel() {

    @ExperimentalCoroutinesApi
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListFundedIdeas(uid: String): LiveData<List<IdeaEntity>?> {
        return firebaseServices.getListFundedIdeas(uid).asLiveData()
    }
}