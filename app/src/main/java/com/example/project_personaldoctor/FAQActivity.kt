package com.example.project_personaldoctor

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import kotlinx.android.synthetic.main.activity_faq.*

class FAQActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        val actionbar = supportActionBar
        actionbar!!.title = "FAQ Page"
        actionbar.setDisplayHomeAsUpEnabled(true)
        CallBtn.setOnClickListener {
            val num = "tel:0876941241"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(num))
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
