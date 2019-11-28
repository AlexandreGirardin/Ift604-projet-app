package com.ift604.projectapp.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val userToken: String?
    //... other data fields that may be accessible to the UI
)
