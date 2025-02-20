package com.example.a3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        auth = Firebase.auth

        val makeLogin: Button = findViewById<Button>(R.id.loginButton)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        makeLogin.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {

                    Toast.makeText(this, task.exception?.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }

        }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser


    }

    private fun checkUser(user: FirebaseUser){
        if(user==null)
        {
            Toast.makeText(this,"No hay usuarios autenticados",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this,"Favor de Autenticarse",Toast.LENGTH_SHORT).show()
        }
    }

}
