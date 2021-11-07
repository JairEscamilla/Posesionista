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
    private val ranges = arrayListOf(0..100, 100..200, 200..300, 300..400, 400..500, 500..600, 600..700, 700..800, 800..900, 900..1000, 1000..10000000)
    val listOfSections = ArrayList<Sections>()

    init {
        for(section in sections) {
            listOfSections.add(Sections(section, arrayListOf()))
        }
    }

    fun agregaCosa(unaCosa: Cosa) {
        val index = getIndexOfSection(unaCosa.valorEnPesos)
        listOfSections[index].list.add(unaCosa)
    }

    fun reorderArrays(cosa: Cosa, prevSectionIndex: Int) {
        val newIndex = getIndexOfSection(cosa.valorEnPesos)
        if(newIndex != prevSectionIndex) {
            val filteredValues = listOfSections[prevSectionIndex].list.filter { it.valorEnPesos in ranges[prevSectionIndex] }
            listOfSections[prevSectionIndex].list.clear()
            listOfSections[prevSectionIndex].list.addAll(filteredValues)
            listOfSections[newIndex].list.add(cosa)
        }
    }

    fun getIndexOfSection(price: Int): Int {
        val position = when(price){
            in 0..100 -> 0
            in 100..200 -> 1
            in 200..300 -> 2
            in 300..400 -> 3
            in 400..500 -> 4
            in 500..600 -> 5
            in 600..700 -> 6
            in 700..800 -> 7
            in 800..900 -> 8
            in 900..1000 -> 9
            else -> 10
        }

        return position
    }
}