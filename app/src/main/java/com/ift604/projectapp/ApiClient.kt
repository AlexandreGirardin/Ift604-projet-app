package com.ift604.projectapp

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import org.jetbrains.anko.doAsync

class ApiClient {

    private val port = 8000
    private val host = "http://10.0.2.2:$port"

    private companion object {
        // Possible routes for API
        private const val ROUTE_REGISTER = "/api/register"
        private const val ROUTE_LOGIN = "/api/login"
        private const val ROUTE_SWIPE = "/api/swipe"
    }

    fun doAsyncRegister(profile: Profile)
    {
        doAsync {
            register(profile)
        }
    }

    private fun register(profile: Profile) {
        Fuel.post(host + ROUTE_REGISTER)
            .jsonBody("{'name' : '${profile.name}'," +
                    "'email' : '${profile.email}'," +
                    "'password' : '${profile.password}'," +
                    "'password_confirmation' : '${profile.password}'}")
            .also { println(it) }
            .response { result -> println("RESULT: $result")}
    }
}