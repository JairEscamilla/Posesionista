package com.example.posesionista

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Cosa(): Parcelable{
    var nombreDeCosa: String = ""
    var valorEnPesos: Int = 0
    var numeroDeSerie: String = UUID.randomUUID().toString().substring(0, 6)
    var fechaDeCreacion: Date = Date()

    constructor(parcel: Parcel) : this(){
        nombreDeCosa = parcel.readString().toString()
        valorEnPesos = parcel.readInt()
        numeroDeSerie = parcel.readString().toString()
        fechaDeCreacion = parcel.readSerializable() as Date
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(nombreDeCosa)
        dest.writeInt(valorEnPesos)
        dest.writeString(numeroDeSerie)
        dest.writeSerializable(fechaDeCreacion)
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
