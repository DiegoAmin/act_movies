package com.example.a3


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MovieList : Fragment() {

    lateinit var datos: ArrayList<Peliculas>
    private val database = FirebaseDatabase.getInstance().getReference("peliculas")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        datos = ArrayList()
        leerBase(view)

        return view
    }

    private fun leerBase(view: View) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                datos.clear()
                snapshot.children.forEach { hijo ->


                    val pelicula = Peliculas(
                        hijo.child("name").value.toString(),
                        hijo.child("year").value.toString(),
                        hijo.child("genre").value.toString(),
                        hijo.child("id").value.toString()
                    )
                    datos.add(pelicula)
                }
                llenarLista(view)


            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Error al leer datos.", error.toException())
            }
        })
    }

    private fun llenarLista(view: View) {
        val list = view.findViewById<ListView>(R.id.List)
        list.adapter = PeliAdapter(requireContext(), datos)

        list.setOnItemClickListener { _, _, position, _ ->
            val peliculaSeleccionada = datos[position]
            mostrarOpcionesEditarEliminar(peliculaSeleccionada, view)
        }
    }

    private fun mostrarOpcionesEditarEliminar(pelicula: Peliculas, view: View) {
        val options = arrayOf("Editar", "Eliminar")

        AlertDialog.Builder(requireContext())
            .setTitle("Selecciona una opción")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editarPelicula(pelicula) // Llamar a la función para editar
                    1 -> eliminarPelicula(pelicula, view) // Llamar a la función para eliminar
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun editarPelicula(pelicula: Peliculas) {
        val editFragment = EditMovie().apply {
            arguments = Bundle().apply {
                // Pasar la película seleccionada como argumento
                putParcelable("pelicula", pelicula)
            }
        }


        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editFragment) // Reemplazar el fragmento actual
            .addToBackStack(null) // Permitir regresar al fragmento anterior
            .commit()
    }

    private fun eliminarPelicula(pelicula: Peliculas, view: View) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar película")
            .setMessage("¿Estás seguro de que quieres eliminar esta película?")
            .setPositiveButton("Eliminar") { _, _ ->

                database.child(pelicula.id).removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Película eliminada", Toast.LENGTH_SHORT).show()

                        leerBase(view)
                    } else {
                        Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
