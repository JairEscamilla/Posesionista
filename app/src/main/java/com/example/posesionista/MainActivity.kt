package com.example.posesionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), TablaDeCosasFragment.InterfazTablaDeCosas {
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

    override fun onCosasSeleccionada(unaCosa: Cosa) {
        // Obtenemos y seteamos una nueva instancia del fragment
        val fragmento = CosaFragment.nuevaInstancia(unaCosa)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragmento).addToBackStack(null).commit()
    }

}