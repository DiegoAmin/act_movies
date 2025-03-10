package com.example.a3

import android.os.Parcel
import android.os.Parcelable

data class Peliculas(
    val name: String,
    val year: String,
    val genre: String,
    val id: String,
    val latitude: String? = null, // Latitud como String (puede ser nulo)
    val longitude: String? = null // Longitud como String (puede ser nulo)
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(), // Leer latitud como String
        parcel.readString()  // Leer longitud como String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(year)
        parcel.writeString(genre)
        parcel.writeString(id)
        parcel.writeString(latitude)  // Guardar latitud como String
        parcel.writeString(longitude) // Guardar longitud como String
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Peliculas> {
        override fun createFromParcel(parcel: Parcel): Peliculas {
            return Peliculas(parcel)
        }

        override fun newArray(size: Int): Array<Peliculas?> {
            return arrayOfNulls(size)
        }
    }

    // Métodos para convertir latitude y longitude a Double
    fun getLatitudeAsDouble(): Double? {
        return latitude?.toDoubleOrNull()
    }

    fun getLongitudeAsDouble(): Double? {
        return longitude?.toDoubleOrNull()
    }
}