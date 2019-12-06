package com.ift604.projectapp.data

import com.ift604.projectapp.ApiClient
import com.ift604.projectapp.DataGenerator
import com.ift604.projectapp.Profile
import com.ift604.projectapp.data.model.LoggedInUser
import java.io.File
import java.io.IOException
import kotlin.random.Random


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val dg = DataGenerator.instance

    fun login(
        username: String,
        password: String,
        latitude: Double,
        longitude: Double,
        profilePic: File
    ): Result<LoggedInUser> {
        try {
            ApiClient.postApiRegister(Profile(
                null,
                dg.generateName(),
                username,
                password,
                0,
                arrayListOf(latitude, longitude),
                profilePic.path,
                dg.generateBio(),
                Random.nextInt(18, 99)
            ))

            val loggedInUser = ApiClient.postApiLogin(username, password)
            return if (loggedInUser.token != "")
                Result.Success(loggedInUser)
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

