package com.example.project_personaldoctor.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.project_personaldoctor.LoginActivity
import com.example.project_personaldoctor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_tools)
        val Email:TextView = root.findViewById(R.id.UserEmailtxt)
        val UID:TextView = root.findViewById(R.id.UserNametxt)
        mAuth = FirebaseAuth.getInstance()
        var user = mAuth!!.currentUser
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val users = firebaseAuth.currentUser
            if (users == null) {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        profileViewModel.text.observe(this, Observer {

            textView.text = it
            Email.text = user!!.email
            UID.text = user!!.uid

        })

//        mAuth = FirebaseAuth.getInstance()
//        var user = mAuth!!.currentUser
//        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
//            val users = firebaseAuth.currentUser
//            if (users == null) {
//                startActivity(Intent(activity, LoginActivity::class.java))
//            }
//            Email.text = user!!.email
//            UID.text = user!!.uid
//        }
       // UserNametxt.text =
        return root
    }
}