package com.bangkit.elevate.data.preference

import android.content.Context
import androidx.core.content.edit

class UserPreference(context: Context) {
    companion object {
        private val PREFS_NAME = "user_pref"
        private val USER_ROLE = "user_role"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setRole(isIdeator: Boolean) {
        preferences.edit {
            putBoolean(USER_ROLE, isIdeator)
        }
    }

    fun getRole(): Boolean {
        return preferences.getBoolean(USER_ROLE, false)
    }
}