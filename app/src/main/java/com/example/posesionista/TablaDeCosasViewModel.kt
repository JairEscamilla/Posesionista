package com.example.posesionista

import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList


class TablaDeCosasViewModel: ViewModel() {
    // Propiedades del view model
    //val inventario = ArrayList<Cosa>()
    private val sections = arrayOf(
        "$0-$100",
        "$100-$200",
        "$200-$300",
        "$300-$400",
        "$400-$500",
        "$500-$600",
        "$600-$700",
        "$700-$800",
        "$800-$900",
        "$900-$1000",
        "+$1000"
    )
    val listOfSections = ArrayList<Sections>()

    init {
        for(section in sections) {
            listOfSections.add(Sections(section, arrayListOf()))
        }
    }

    fun agregaCosa(unaCosa: Cosa) {
        if(unaCosa.valorEnPesos >= 100) {
            listOfSections[1].list.add(unaCosa)
        }else {
            listOfSections[0].list.add(unaCosa)
        }

    }
}