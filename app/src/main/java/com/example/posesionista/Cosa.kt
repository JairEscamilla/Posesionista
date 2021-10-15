package com.example.posesionista

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

class Cosa(): Parcelable{
    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var nombreDeCosa: String = ""
    var valorEnPesos: Int = 0
    var numeroDeSerie: String = UUID.randomUUID().toString().substring(0, 6)
    var fechaDeCreacion: String = simpleDateFormat.format(Date())



    constructor(parcel: Parcel) : this(){
        nombreDeCosa = parcel.readString().toString()
        valorEnPesos = parcel.readInt()
        numeroDeSerie = parcel.readString().toString()
        fechaDeCreacion = parcel.readString().toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(nombreDeCosa)
        dest.writeInt(valorEnPesos)
        dest.writeString(numeroDeSerie)
        dest.writeString(fechaDeCreacion)
    }

    companion object CREATOR : Parcelable.Creator<Cosa> {
        override fun createFromParcel(source: Parcel): Cosa {
            return Cosa(source)
        }

        override fun newArray(size: Int): Array<Cosa?> {
            return arrayOfNulls(size)
        }
    }
}
