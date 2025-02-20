package com.example.a3

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    lateinit var  datos : ArrayList<Peliculas>

    val database = Firebase.database
    val myRef = database.getReference("peliculas")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)

       // llenarlista()
        leerBase()

    }

    private fun leerBase(){
        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value
                Log.d(TAG, "Value is: " + value)
                datos = ArrayList<Peliculas>()


                snapshot.children.forEach{
                    hijo ->
                    var pelicula = Peliculas(hijo.child("name").value.toString(),hijo.child("year").value.toString(),hijo.child("genre").value.toString(),hijo.child("id").value.toString())

                    datos.add(pelicula)
                }
                llenarlista()
            }


            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })



    }

    private fun llenarlista() {


        var list = findViewById<ListView>(R.id.List)
        list.adapter = PeliAdapter(this,datos)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.Exit)
        {
            startActivity(Intent(this, Principal::class.java))
            Toast.makeText(this,"Salir de la APP",Toast.LENGTH_SHORT).show()
        }
        else if(item.itemId == R.id.Profile)
        {
            Toast.makeText(this,"Ir al Perfil",Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}