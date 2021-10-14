package com.example.posesionista

import androidx.lifecycle.ViewModel
import java.util.*


class TablaDeCosasViewModel: ViewModel() {
    val inventario = mutableListOf<Cosa>()
    val nombres = arrayOf("Telefono", "Pan", "Playera")
    var adjetivos = arrayOf("Gris", "Suave", "Comoda")

    init {
        for(i in 0 until 100){
            val cosa = Cosa()
            val nombreAzar = nombres.random()
            val adjetivoAzar = adjetivos.random()
            val precioAzar = Random().nextInt(1001)
            cosa.nombreDeCosa = "$nombreAzar $adjetivoAzar"
            cosa.valorEnPesos = precioAzar
            inventario += cosa
        }
    }
}