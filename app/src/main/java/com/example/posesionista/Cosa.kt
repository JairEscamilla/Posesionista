package com.example.posesionista

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

// Declaracion de la clase Cosa
class Cosa(): Parcelable{
    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Formateador para la fecha
    var nombreDeCosa: String = ""
    var valorEnPesos: Int = 0
    var numeroDeSerie = ""
    var fechaDeCreacion: String = simpleDateFormat.format(Date())
    var idCosa = UUID.randomUUID().toString().substring(0, 6)



    constructor(parcel: Parcel) : this(){
        nombreDeCosa = parcel.readString().toString()
        valorEnPesos = parcel.readInt()
        numeroDeSerie = parcel.readString().toString()
        fechaDeCreacion = parcel.readString().toString()
        idCosa = parcel.readString().toString()
    }

    // Funcion para definir la clase abstracta
    override fun describeContents(): Int {
        return 0
    }

    // Funcion que se usa para escribir datos a parcel
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(nombreDeCosa)
        dest.writeInt(valorEnPesos)
        dest.writeString(numeroDeSerie)
        dest.writeString(fechaDeCreacion)
        dest.writeString(idCosa)
    }

    // Companion object para crear el Parcelable de los datos que se quieren comunicar
    companion object CREATOR : Parcelable.Creator<Cosa> {
        override fun createFromParcel(source: Parcel): Cosa {
            return Cosa(source)
        }

        override fun newArray(size: Int): Array<Cosa?> {
            return arrayOfNulls(size)
        }
    }
}
