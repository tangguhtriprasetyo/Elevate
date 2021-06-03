package com.bangkit.elevate.ui.dashboard.funder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.elevate.data.FundedIdeasEntity
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.firebase.FirebaseServices
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FunderProgressViewModel : ViewModel() {

    @ExperimentalCoroutinesApi
    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var list = MutableLiveData<List<FundedIdeasEntity>?>()
    private var _listIdeasData = MutableLiveData<List<IdeaEntity>>()
    private var isTrueFunded = MutableLiveData<Boolean>()
    val listIdeasData: LiveData<List<IdeaEntity>> = _listIdeasData

    fun getListIdeas(): LiveData<List<IdeaEntity>?> {
        return firebaseServices.getListIdeas().asLiveData()
    }

    fun setListFundedIdeas(uid: String): LiveData<List<FundedIdeasEntity>?> {
        return firebaseServices.getListFundedIdeas(uid).asLiveData()
    }

    fun getFundedIdea(listFunded: List<FundedIdeasEntity>): LiveData<List<IdeaEntity>> {
        return firebaseServices.getFundedIdeasData(listFunded)
    }
}