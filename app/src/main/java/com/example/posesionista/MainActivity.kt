package com.example.posesionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity(), TablaDeCosasFragment.InterfazTablaDeCosas {
    private var cosaActual: Cosa = Cosa()
    private var tablaDeCosasViewModel: TablaDeCosasViewModel? = null
    private var isEditing: Boolean = false
    private var currentCosaPosition: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Se obtiene el fragmento actual
        val fragmentoActual = supportFragmentManager.findFragmentById(R.id.fragment_container)
        // Si no existe, seteamos el fragmento de la Tabla de Cosas
        if(fragmentoActual == null){
            val fragmento = TablaDeCosasFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragmento).commit()
        }
    }

    override fun onCosasSeleccionada(unaCosa: Cosa, tablaCosas: TablaDeCosasViewModel, editing: Boolean) {
        // Obtenemos y seteamos una nueva instancia del fragment
        cosaActual = unaCosa
        tablaDeCosasViewModel = tablaCosas
        isEditing = editing
        currentCosaPosition = tablaCosas.getIndexOfSection(unaCosa.valorEnPesos)
        val fragmento = CosaFragment.nuevaInstancia(unaCosa)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragmento).addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if(cosaActual.nombreDeCosa.isEmpty()) {
            Toast.makeText(this, "El campo nombre no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else {
            if(!isEditing) {
                tablaDeCosasViewModel?.agregaCosa(cosaActual)
            }else{
                tablaDeCosasViewModel?.reorderArrays(cosaActual, currentCosaPosition)
            }

            super.onBackPressed()
        }
    }

}