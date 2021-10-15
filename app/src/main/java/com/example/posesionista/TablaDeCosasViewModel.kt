package com.example.posesionista

import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList


class TablaDeCosasViewModel: ViewModel() {
    // Propuedades del view model
    val inventario = ArrayList<Cosa>()
    private val nombres = arrayOf("Telefono", "Pan", "Playera")
    private var adjetivos = arrayOf("Gris", "Suave", "Comoda")

    init {
        // Generamos cosas al azar
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