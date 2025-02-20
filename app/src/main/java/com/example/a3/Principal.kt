package com.example.a3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a3.R
import com.google.firebase.auth.FirebaseAuth

class Principal : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        auth = FirebaseAuth.getInstance()

        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)


        val user = auth.currentUser
        if (user != null) {
            welcomeTextView.text = "¡See you Later, ${user.email}!"
        }


        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}