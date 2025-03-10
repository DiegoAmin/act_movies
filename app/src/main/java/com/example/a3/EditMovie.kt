package com.example.a3

import android.content.pm.PackageManager
import android.Manifest
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase

class EditMovie : Fragment() {

    private val REQUEST_LOCATION_PERMISSION = 1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    private lateinit var editName: EditText
    private lateinit var editYear: EditText
    private lateinit var editGenre: EditText
    private lateinit var btnSave: Button

    private lateinit var pelicula: Peliculas
    private val database = FirebaseDatabase.getInstance().getReference("peliculas")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_movie, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        editName = view.findViewById(R.id.editName)
        editYear = view.findViewById(R.id.editYear)
        editGenre = view.findViewById(R.id.editGenre)
        btnSave = view.findViewById(R.id.btnSave)


        pelicula = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("pelicula", Peliculas::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("pelicula")
        } ?: throw IllegalStateException("Película no proporcionada")


        editName.setText(pelicula.name)
        editYear.setText(pelicula.year)
        editGenre.setText(pelicula.genre)


        btnSave.setOnClickListener {
            guardarCambios()
        }

        verificarPermisosUbicacion()
        obtenerUbicacion()

        return view
    }

    private fun verificarPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {

            obtenerUbicacion()
        }
    }
    private fun obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    Log.d("EditMovieFragment", "Ubicación obtenida: $currentLatitude, $currentLongitude")
                } else {
                    Log.d("EditMovieFragment", "No se pudo obtener la ubicación")
                    // Si no se obtiene la ubicación, puedes asignar null
                    currentLatitude = null
                    currentLongitude = null
                }
            }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                obtenerUbicacion()
            } else {

                Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun guardarCambios() {
        val name = editName.text.toString()
        val year = editYear.text.toString()
        val genre = editGenre.text.toString()


        if (name.isEmpty() || year.isEmpty() || genre.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }


        val peliculaActualizada = Peliculas(
            name,
            year,
            genre,
            pelicula.id,
            currentLatitude.toString(), // Latitud (puede ser null)
            currentLongitude.toString() // Longitud (puede ser null)
        )


        database.child(pelicula.id).setValue(peliculaActualizada).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Película actualizada", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // Regresar al fragmento anterior
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}