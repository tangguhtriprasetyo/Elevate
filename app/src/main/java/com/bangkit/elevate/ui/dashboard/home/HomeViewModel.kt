package com.bangkit.elevate.ui.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.firebase.FirebaseServices
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListIdeas(filterQuery: String): LiveData<List<IdeaEntity>?> {
        return firebaseServices.getListIdeas(filterQuery).asLiveData()
    }
}