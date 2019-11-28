package com.ift604.projectapp

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.getAs
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject

class ApiClient {

    private val port = "8899"
    private val url = "http://172.105.99.204:$port"
    var client = ConnectedUser("", Profile(0, "", "", "", 0, "", listOf("https://source.unsplash.com/HN-5Z6AmxrM/600x800"), "", "", 0))

    private companion object {
        // Possible routes for API
        private const val ROUTE_REGISTER = "/api/register"
        private const val ROUTE_LOGIN = "/api/login"
        private const val ROUTE_SWIPE = "/api/swipe"
        private const val ROUTE_USERS = "/api/users"
    }

    fun register(profile: Profile)
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
                    .response { result -> println("RESULT: $result")}
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }
    }

    fun postApiLogin(profile: Profile)
    {
        runBlocking {
            postLogin(profile)
        }
    }

    fun getApiSwipe(): JSONArray {
        var swipeableUsers = JSONArray()
        runBlocking {
            if (client.token == "")
                postLogin(client.profile)
            swipeableUsers = getSwipeableUsers()
        }
        //println("APICLIENT: $swipeableUsers")
        return swipeableUsers
    }

    private fun postLogin(profile: Profile) {
        var token = ""
        Fuel.post(url + ROUTE_LOGIN)
            .jsonBody(
                "{\n" +
                        "\t\"email\" : \"${profile.email}\",\n" +
                        "\t\"password\" : \"${profile.password}\"\n" +
                        "}"
            )
            .also { println(it.url) }
            .responseString { result ->
                val obj = JSONObject(result.get())
                token = obj.get("access_token") as String
            }.join()
        //println("TOKEN: $token, PROFILE: ${profile.name}")
        client = ConnectedUser(token, profile)
    }

    private fun getSwipeableUsers(): JSONArray {
        var jsonArray = JSONArray()
        try {
            Fuel.get(url + ROUTE_SWIPE)
                .authentication()
                .bearer(client.token)
                .responseString { result ->
                    jsonArray = JSONArray(result.get())
                }.join()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message!!)
        }

        //println("GET - /api/swipe --> $jsonArray")
        return jsonArray
    }

    private fun getUsers(): JSONArray {
        var jsonArray = JSONArray()
        try {
            Fuel.get(url + ROUTE_USERS)
                .authentication()
                .bearer(client.token)
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


}