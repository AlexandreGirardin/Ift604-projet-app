package com.ift604.projectapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ift604.projectapp.ui.login.LoginActivity


class MainLauncher: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("SendUdeS", Context.MODE_PRIVATE)

        sp.edit().putString("token", null).apply()

        val activityIntent: Intent = if ( sp.getString("token", null) != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(activityIntent)
        finish()
    }
}