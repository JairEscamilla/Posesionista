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
        when(unaCosa.valorEnPesos){
            in 0..100 -> listOfSections[0].list.add(unaCosa)
            in 100..200 -> listOfSections[1].list.add(unaCosa)
            in 200..300 -> listOfSections[2].list.add(unaCosa)
            in 300..400 -> listOfSections[3].list.add(unaCosa)
            in 400..500 -> listOfSections[4].list.add(unaCosa)
            in 500..600 -> listOfSections[5].list.add(unaCosa)
            in 600..700 -> listOfSections[6].list.add(unaCosa)
            in 700..800 -> listOfSections[7].list.add(unaCosa)
            in 800..900 -> listOfSections[8].list.add(unaCosa)
            in 900..1000 -> listOfSections[9].list.add(unaCosa)
            else -> listOfSections[10].list.add(unaCosa)
        }
    }
}