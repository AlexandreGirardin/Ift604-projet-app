package com.ift604.projectapp.data

import com.ift604.projectapp.ApiClient
import com.ift604.projectapp.DataGenerator
import com.ift604.projectapp.Profile
import com.ift604.projectapp.data.model.LoggedInUser
import java.io.IOException
import kotlin.random.Random

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val dg = DataGenerator.instance

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val profile = Profile(
                null,
                dg.generateName(),
                username,
                password,
                0,
                "",
                dg.generateBio(),
                Random.nextInt(18, 99)
            )

            ApiClient.postApiRegister(profile)

            val user = LoggedInUser(profile, "")
            val token = ApiClient.postApiLogin(user)
            user.token = token
            println((ApiClient.loggedInUser)?.profile?.age)
            return if (user.token != "")
            {
                Result.Success(user)
            }
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

