package com.epitech.epicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var clientId: String = "979976772a2d967"
    var clientSecret: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_connect.setOnClickListener {
            Toast.makeText(this, "Button was clicked!", Toast.LENGTH_SHORT).show()

            val email: String = editText_mail.text.toString()
            val password: String = editText_password.text.toString()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
