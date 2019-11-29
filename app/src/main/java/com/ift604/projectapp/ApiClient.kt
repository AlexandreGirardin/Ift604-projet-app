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

class ApiClient {

    private val port = "8899"
    private val url = "http://172.105.99.204:$port"
    private var token = ""

    companion object {
        val instance = ApiClient()

        // Possible routes for API
        private const val ROUTE_REGISTER = "/api/register"
        private const val ROUTE_LOGIN = "/api/login"
        private const val ROUTE_SWIPE = "/api/swipe"
        private const val ROUTE_USERS = "/api/users"
    }

    fun postApiRegister(profile: Profile)
    {
        try {
            doAsync {
                Fuel.post(url + ROUTE_REGISTER)
                    .jsonBody("{" +
                            "\"name\": \"${profile.name}\"," +
                            "\"email\":\"${profile.email}\"," +
                            "\"password\":\"${profile.password}\",\n" +
                            "\"password_confirmation\": \"${profile.password}\"\n" +
                            "}")
                    .response { result ->
                        println("RESULT: $result")
                    }.join()
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }
    }

    fun postApiLogin(user: LoggedInUser): String {
        var loginToken = ""
        runBlocking {
            loginToken = postLogin(user.profile.email, user.profile.password)
        }

        return loginToken
    }

    fun getApiSwipe(): JSONArray {
        var swipeableUsers = JSONArray()
        runBlocking {
            if (token != "")
                swipeableUsers = getSwipeableUsers()
        }

        return swipeableUsers
    }

    private fun postLogin(email: String, password: String): String {
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
                }.join()

        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        return token
    }

    private fun getSwipeableUsers(): JSONArray {
        var jsonArray = JSONArray()
        try {
            Fuel.get(url + ROUTE_SWIPE)
                .authentication()
                .bearer(token)
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
                .bearer(token)
                .also { println(it.url) }
                .responseString { result -> println()
                    jsonArray = JSONArray(result.getAs<String>())
                }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        println("JSONArray: $jsonArray")
        return jsonArray
    }

    fun getUrl(): String {
        return url
    }


}