package com.ift604.projectapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ift604.projectapp.ui.login.LoginActivity


class MainLauncher: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("SendUdeS", Context.MODE_PRIVATE)
        /*val activityIntent: Intent = if ( sp.getString("token", null) != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(activityIntent)
        finish()
    }
}