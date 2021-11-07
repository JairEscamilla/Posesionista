package com.example.posesionista

import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList


class TablaDeCosasViewModel: ViewModel() {
    // Propiedades del view model
    //val inventario = ArrayList<Cosa>()
    private val sections = arrayOf(
        "$0-$99",
        "$100-$199",
        "$200-$299",
        "$300-$399",
        "$400-$499",
        "$500-$599",
        "$600-$699",
        "$700-$799",
        "$800-$899",
        "$900-$999",
        "+$1000"
    )
    private val ranges = arrayListOf(0..99, 100..199, 200..299, 300..399, 400..499, 500..599, 600..699, 700..799, 800..899, 900..999, 1000..10000000)
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
            in 0..99 -> 0
            in 100..199 -> 1
            in 200..299 -> 2
            in 300..399 -> 3
            in 400..499 -> 4
            in 500..599 -> 5
            in 600..699 -> 6
            in 700..799 -> 7
            in 800..899 -> 8
            in 900..999 -> 9
            else -> 10
        }

        return position
    }
}