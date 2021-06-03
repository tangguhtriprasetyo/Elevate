package com.bangkit.elevate.data

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    var activeIdea: Boolean? = false,
    var address: String? = null,
    var avatar: String? = null,
    var balance: Long = 0,
    var email: String? = null,
    var phone: String? = null,
    var totalFunded: Long = 0,
    var uid: String? = null,
    var username: String? = null,
    @get:Exclude
    var isAuthenticated: Boolean? = null,
    @get:Exclude
    var isNew: Boolean? = null,
    @get:Exclude
    var isCreated: Boolean? = null
) : Parcelable
