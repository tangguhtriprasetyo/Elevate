package com.bangkit.elevate.ui.dashboard.home

import com.bangkit.elevate.data.IdeaEntity

interface HomeClickCallback {
    fun onItemClicked(ideas: IdeaEntity)
}