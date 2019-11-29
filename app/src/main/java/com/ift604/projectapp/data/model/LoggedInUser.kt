package com.ift604.projectapp.data.model

import com.ift604.projectapp.Profile

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val profile: Profile,
    var token: String
)
