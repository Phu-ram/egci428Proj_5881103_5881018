package com.example.project_personaldoctor


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.project_personaldoctor.Helper.HTTPHelper
import com.example.project_personaldoctor.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.signUpBtn


class LoginActivity : AppCompatActivity() {
    //private val REQUEST_CODE = 1
    var auth: FirebaseAuth? = null
    private val TAG: String = "Login Activity"
    private var bitmap: Bitmap?? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login)
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.doctor)
        imageView.setImageBitmap(bitmap)
        val intent = Intent(this,HomePageActivity::class.java)
        if(auth!!.currentUser!=null){
            startActivity(intent)
            finish()
        }

        signInBtn.setOnClickListener {
            var umail = txtUsername.text.toString()
            var pass = txtPassword.text.toString()
            if (!umail.isNullOrEmpty() && !pass.isNullOrEmpty()) {
                auth!!.signInWithEmailAndPassword(umail, pass).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        if (pass.length < 6) {
                            Toast.makeText(applicationContext, ".", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Password must be at least 8 characters")
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Authentication Failed:" + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "Authentication Failed:" + task.exception)
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sign in successful!!.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "Sign in successfully!!")
                        startActivity(intent)
                    }
                }
            }

        }
        signUpBtn.setOnClickListener {
            val RegisterPage = Intent(this, SignUpActivity::class.java)
            startActivity(RegisterPage)
        }
        contactBtn.setOnClickListener {
            val FAQPage = Intent(this,FAQActivity::class.java)
            startActivity(FAQPage)
        }
        }


    }





