package com.bangkit.elevate.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdeaEntity(
    var brandName: String? = null,
    var businessIdea: String? = null,
    var currentFund: Int = 0,
    var description: String? = null,
    var ideatorUid: String? = null,
    var location: String? = null,
    var logoFile: String? = null,
    var proposalFile: String? = null,
    var requiredCost: Int = 0,
    var status: String? = null,
    var term1File: String? = null,
    var term1Unlocked: Boolean = false,
    var term2File: String? = null,
    var term2Unlocked: Boolean = false,
    var term3File: String? = null,
    var term3Unlocked: Boolean = false
) : Parcelable
