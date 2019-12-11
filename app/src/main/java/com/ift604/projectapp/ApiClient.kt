package com.ift604.projectapp

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.getAs
import com.ift604.projectapp.data.model.LoggedInUser
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject


object ApiClient {

    private val port = "8899"
    private val url = "http://172.105.99.204:$port"
    var loggedInUser: LoggedInUser? = null

    // Possible routes for API
    private const val ROUTE_REGISTER = "/api/register"
    private const val ROUTE_LOGIN = "/api/login"
    private const val ROUTE_SWIPE = "/api/swipe"
    private const val ROUTE_USERS = "/api/users"
    private const val ROUTE_MATCHES = "/api/matches"

    /**
     * Async functions to access used by other classes to make calls to the API.
     */

    fun postApiRegister(profile: Profile) {
        runBlocking {
            register(profile)
        }
    }

    fun postApiLogin(email: String, password: String): LoggedInUser? {
        runBlocking {
            loggedInUser = login(email, password)
        }

        return loggedInUser
    }

    fun getApiSwipe(): JSONArray {
        var swipeableUsers = JSONArray()
        runBlocking {
            if (loggedInUser!!.token != "")
                swipeableUsers = getSwipeableUsers()
        }

        return swipeableUsers
    }

    fun postApiLike(userId: Int) {
        runBlocking {
            like(userId)
        }
    }

    fun getApiMatches(): JSONArray {
        var matches = JSONArray()
        runBlocking {
            matches = fetchMatches()
        }

        println("MATCHES: $matches")
        return matches
    }

    /**
     * Functions that make the actual calls to the API.
     */

    private fun register(profile: Profile)
    {
        try {
            val formData = listOf(
                "name" to profile.name,
                "email" to profile.email,
                "age" to profile.age,
                "bio" to profile.bio,
                "latitude" to profile.position[0],
                "longitude" to profile.position[1],
                "password" to profile.password,
                "password_confirmation" to profile.password)

            if (profile.photo != "")
                Fuel.upload(url + ROUTE_REGISTER, parameters = formData)
                    .add(FileDataPart.from(profile.photo, name = "photo"))
                    .also { println(it) }
                    .response { result -> }.join()
            else
                Fuel.upload(url + ROUTE_REGISTER, parameters = formData)
                    .also { println(it) }
                    .response { result -> }.join()
        } catch (e: Exception) {
            println(e.message!!)
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
                .bearer(loggedInUser!!.token)
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
                .bearer(loggedInUser!!.token)
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
                .bearer(loggedInUser!!.token)
                .jsonBody("{\"user_id\": $userId}")
                .also { println("${it.url}, USER_ID: $userId") }
                .responseString { result -> }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }
    }

    private fun fetchMatches(): JSONArray {
        var matches = JSONArray()
        try {
            Fuel.get(url + ROUTE_MATCHES)
                .authentication()
                .bearer(loggedInUser!!.token)
                .also { println("${it.url}") }
                .responseString { result ->
                    matches = JSONArray(result.getAs<String>())
                }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        return matches
    }

    /**
     * Other useful functions.
     */

    fun getUrl(): String {
        return url
    }

    fun logout() {
        loggedInUser = null
    }
}