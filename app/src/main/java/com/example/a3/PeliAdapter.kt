package com.example.a3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class PeliAdapter(private val context: Context, private val arrayList: ArrayList<Peliculas>)
    : ArrayAdapter<Peliculas>(context, R.layout.item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item, null)


        val nameTextView = view.findViewById<TextView>(R.id.Name)
        val yearTextView = view.findViewById<TextView>(R.id.Year)
        val genreTextView = view.findViewById<TextView>(R.id.Genre)
        val iconPelicula = view.findViewById<ImageView>(R.id.iconPelicula)


        val pelicula = arrayList[position]


        nameTextView.text = pelicula.name
        yearTextView.text = pelicula.year
        genreTextView.text = pelicula.genre


        when (pelicula.genre) {
            "accion" -> {
                iconPelicula.setImageResource(R.drawable.ic_action)
            }
            "drama" -> {
                iconPelicula.setImageResource(R.drawable.ic_drama)
            }
            "comedia" -> {
                iconPelicula.setImageResource(R.drawable.ic_comedy)
            }

            else -> {

                iconPelicula.setImageResource(R.drawable.ic_default)
            }
        }

        return view
    }
}