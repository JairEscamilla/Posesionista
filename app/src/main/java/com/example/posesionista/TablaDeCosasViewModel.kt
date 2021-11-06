package com.example.posesionista

import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList


class TablaDeCosasViewModel: ViewModel() {
    // Propuedades del view model
    val inventario = ArrayList<Cosa>()

    fun agregaCosa(unaCosa: Cosa) {
        inventario.add(unaCosa)
    }
}