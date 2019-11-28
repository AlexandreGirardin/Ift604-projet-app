package com.ift604.projectapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.ift604.projectapp.data.LoginDataSource
import com.ift604.projectapp.data.LoginRepository
import com.ift604.projectapp.ui.login.LoginActivity
import com.ift604.projectapp.ui.login.LoginViewModel

class ProfileFragment : Fragment() {
    private lateinit var settingsBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//        Update view here
//        val profilePicture = view.findViewById<ImageView>(R.id.profilePicture)

        settingsBtn = view.findViewById(R.id.settingsBtn)
        settingsBtn.setOnClickListener {
            LoginRepository( dataSource = LoginDataSource()).logout()
            val sp = activity?.getSharedPreferences("SendUdeS", Context.MODE_PRIVATE)?.edit()
            sp?.remove("token")
            sp?.apply()
            val activityIntent = Intent(context, LoginActivity::class.java)
            startActivity(activityIntent)
            activity?.finish()
        }

        return view
    }

    private fun fetchUserProfile() {
//        We may only need the user picture here
//        fetch data here
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment()
    }
}
