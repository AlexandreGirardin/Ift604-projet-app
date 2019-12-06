package com.ift604.projectapp.ui.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ift604.projectapp.LikeService
import com.ift604.projectapp.MainActivity

import com.ift604.projectapp.R
import java.io.File
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var profilePic: File = File("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0x0)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0x0)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0x0)
        }

        val intent = Intent(this, LikeService::class.java)
        startService(intent)

        setContentView(R.layout.activity_login)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var latitude = 0.0
        var longitude = 0.0
        var city = ""

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val tvCity = findViewById<TextView>(R.id.tvCity)
        val profilePicBtn = findViewById<Button>(R.id.profilePicBtn)

        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                println("LATITUDE: ${location.latitude}, LONGITUDE: ${location.longitude}")

                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)
                city = addresses[0].getAddressLine(0)
                tvCity.text = city
            }
        }

        if (city == "")
            tvCity.text = "Position introuvable"

        profilePicBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("image/jpeg", "image/png"))
            startActivityForResult(intent, 1)
        }

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                startMainActivity(loginResult.success)
                finish()
            }

            setResult(Activity.RESULT_OK)
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(username.text.toString(), password.text.toString(),
                            latitude, longitude, profilePic
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString(),
                    latitude, longitude, profilePic
                )
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 1 && data != null)
            {
                val selectedImage: Uri? = data.data
                val profilePicView = findViewById<ImageView>(R.id.profilePicView)
                profilePicView.setImageURI(selectedImage);
                profilePic = File(convertMediaUriToPath(selectedImage)!!)

                println(profilePic.exists())
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun convertMediaUriToPath(uri: Uri?): String? {
	    val proj = arrayOf(MediaStore.Images.Media.DATA)
	    val cursor: Cursor? = contentResolver.query(uri!!, proj,  null, null, null);
	    val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor?.moveToFirst();
	    val path: String? = cursor?.getString(column_index!!);
	    cursor?.close();
	    return path;
	}

    private fun startMainActivity(model: LoggedInUserView) {
        val sp = getSharedPreferences("SendUdeS", Context.MODE_PRIVATE).edit()
        sp.putString("token", model.userToken)
        sp.apply()
        val activityIntent = Intent(this, MainActivity::class.java)
        startActivity(activityIntent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
