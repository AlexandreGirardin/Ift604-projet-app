package com.ift604.projectapp.data

import com.ift604.projectapp.ApiClient
import com.ift604.projectapp.Profile
import com.ift604.projectapp.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {



    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val user = LoggedInUser(Profile(0, "", username, password, 0, "", "https://source.unsplash.com/HN-5Z6AmxrM/600x800", "", 18), "")
            val token = ApiClient.instance.postApiLogin(user)
            user.token = token
            return if (user.token != "")
                Result.Success(user)
            else
                Result.Error(IOException("Token is empty"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

