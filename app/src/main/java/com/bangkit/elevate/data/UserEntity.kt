package com.bangkit.elevate.data

data class UserEntity(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null
)
