package com.ift604.projectapp

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.getAs
import com.ift604.projectapp.data.model.LoggedInUser
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject

object ApiClient {

    private val port = "8899"
    private val url = "http://172.105.99.204:$port"
    lateinit var loggedInUser: LoggedInUser

    // Possible routes for API
    private const val ROUTE_REGISTER = "/api/register"
    private const val ROUTE_LOGIN = "/api/login"
    private const val ROUTE_SWIPE = "/api/swipe"
    private const val ROUTE_USERS = "/api/users"

    /**
     * Async functions to access used by other classes to make calls to the API.
     */

    fun postApiRegister(profile: Profile) {
        runBlocking {
            register(profile)
        }
    }

    fun postApiLogin(email: String, password: String): LoggedInUser {
        runBlocking {
            loggedInUser = login(email, password)
        }

        return loggedInUser
    }

    fun getApiSwipe(): JSONArray {
        var swipeableUsers = JSONArray()
        runBlocking {
            if (loggedInUser.token != "")
                swipeableUsers = getSwipeableUsers()
        }

        return swipeableUsers
    }

    fun postApiLike(userId: Int) {
        runBlocking {
            like(userId)
        }
    }

    /**
     * Functions that make the actual calls to the API.
     */

    private fun register(profile: Profile)
    {
        try {
            Fuel.post(url + ROUTE_REGISTER)
                .jsonBody("{" +
                        "\"name\": \"${profile.name}\"," +
                        "\"email\":\"${profile.email}\"," +
                        "\"age\": \"${profile.age}\"," +
                        "\"bio\": \"${profile.bio}\"," +
                        "\"password\":\"${profile.password}\",\n" +
                        "\"password_confirmation\": \"${profile.password}\"\n" +
                        "}")
                .also { println(it.url) }
                .response { result -> }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }
    }

    private fun login(email: String, password: String): LoggedInUser {
        var token = ""
        var profile = Profile()
        try {
            Fuel.post(url + ROUTE_LOGIN)
                .jsonBody(
                    "{\n" +
                            "\t\"email\" : \"$email\",\n" +
                            "\t\"password\" : \"$password\"\n" +
                            "}"
                )
                .also { println(it.url) }
                .responseString { result ->
                    val obj = JSONObject(result.get())
                    token = obj.get("access_token") as String
                    profile = Profile(obj.getJSONObject("user"))
                    profile.email = email
                    profile.password = password
                }.join()

        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        return LoggedInUser(profile, token)
    }

    private fun getSwipeableUsers(): JSONArray {
        var jsonArray = JSONArray()
        try {
            Fuel.get(url + ROUTE_SWIPE)
                .authentication()
                .bearer(loggedInUser.token)
                .also { println(it.url) }
                .responseString { result ->
                    jsonArray = JSONArray(result.get())
                }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        return jsonArray
    }

    private fun getUsers(): JSONArray {
        var jsonArray = JSONArray()
        try {
            Fuel.get(url + ROUTE_USERS)
                .authentication()
                .bearer(loggedInUser.token)
                .also { println(it.url) }
                .responseString { result -> println()
                    jsonArray = JSONArray(result.getAs<String>())
                }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        return jsonArray
    }

    private fun like(userId: Int) {
        try {
            Fuel.post(url + ROUTE_SWIPE)
                .authentication()
                .bearer(loggedInUser.token)
                .jsonBody("{\"user_id\": $userId}")
                .also { println("${it.url}, USER_ID: $userId") }
                .responseString { result -> }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }
    }

    /**
     * Other useful functions.
     */

    fun getUrl(): String {
        return url
    }


}