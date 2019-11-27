package com.example.project_personaldoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    private val TAG: String = "Register Activity"

    override  fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        if (auth!!.currentUser != null){
            startActivity(Intent(this@SignUpActivity,HomePageActivity::class.java))
            finish()
        }

        registerBtn.setOnClickListener {
            val email = regis_emailtxt.text.toString()
            val password = regis_passwordtxt.text.toString()

            if(email.isEmpty()){
                Toast.makeText(applicationContext,"Please enter your email address.",Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Email was empty!")
                return@setOnClickListener
            }
            if(password.isEmpty()){
                Toast.makeText(applicationContext,"Please enter your password.",Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Password was empty!!")
                return@setOnClickListener
            }
            auth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if(!task.isSuccessful){
                    if(password.length<6){
                       Log.d(TAG,"Enter password less then 6 characters.")
                        Toast.makeText(applicationContext,"Password must be more than 8 characters.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,"Authenication failed:"+ task.exception, Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"Authentication Failed: "+ task.exception)
                    }
                }else{
                    Log.d(TAG,"Create account successfully!")
                    Toast.makeText(applicationContext,"Created User Successfully!!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpActivity,HomePageActivity::class.java))
                    finish()
                }
            }
        }
//        registerBtn.setOnClickListener { startActivity(Intent(this,LoginActivity::class.java)) }
    }
}
