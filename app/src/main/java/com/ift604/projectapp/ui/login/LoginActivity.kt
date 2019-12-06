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
    private var latitude = 0.0
    private var longitude = 0.0
    private var city = ""

    private lateinit var tvCity: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var loading: ProgressBar
    private lateinit var profilePicBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(locationPermissionIsGranted())
            getLocationInformations()

        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        loading = findViewById(R.id.loading)
        profilePicBtn = findViewById(R.id.profilePicBtn)
        tvCity = findViewById(R.id.tvCity)

        if (city == "")
            tvCity.text = ""
        else
            tvCity.text = city

        profilePicBtn.setOnClickListener {
            if (externalStoragePermissionIsGranted())
                chooseProfilePicture()
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

    private fun locationPermissionIsGranted(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            false;
        } else
            true;
    }

    private fun externalStoragePermissionIsGranted(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
            false
        } else
            true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        getLocationInformations()
                } else
                    println("Permission Denied for location.")
                return
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        chooseProfilePicture()
                } else
                    println("Permission Denied for external storage")
                return
            }
        }
    }

    private fun getLocationInformations() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)
                city = addresses[0].getAddressLine(0)
                tvCity.text = city
            }
        }
    }

    private fun chooseProfilePicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("image/jpeg", "image/png"))
        startActivityForResult(intent, 1)
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
