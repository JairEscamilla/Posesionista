package com.example.posesionista

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class CosaFragment: Fragment() {
    private lateinit var cosa: Cosa
    private lateinit var campoNombre: EditText
    private lateinit var campoPrecio: EditText
    private lateinit var campoSerie: EditText
    private lateinit var campoFecha: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosa = Cosa()
        cosa = arguments?.getParcelable("COSA_RECIBIDA")!!

    }

    override fun onStart() {
        super.onStart()
        val observador = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.hashCode() == campoNombre.text.hashCode()){
                    cosa.nombreDeCosa = s.toString()
                }
                if(s.hashCode() == campoPrecio.text.hashCode()){
                    if(s!= null){
                        if(s.isEmpty()){
                            cosa.valorEnPesos = 0
                        } else{
                            cosa.valorEnPesos = s.toString().toInt()
                        }
                    }

                }
                if(s.hashCode() == campoSerie.text.hashCode()){
                    cosa.numeroDeSerie = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        campoNombre.addTextChangedListener(observador)
        campoPrecio.addTextChangedListener(observador)
        campoSerie.addTextChangedListener(observador)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.cosa_fragment, container, false)
        campoNombre = vista.findViewById(R.id.nombre_cosa) as EditText
        campoPrecio = vista.findViewById(R.id.precio_cosa) as EditText
        campoSerie = vista.findViewById(R.id.serie_cosa) as EditText
        campoFecha = vista.findViewById(R.id.labelFecha) as TextView
        campoNombre.setText(cosa.nombreDeCosa)
        campoPrecio.setText(cosa.valorEnPesos.toString())
        campoSerie.setText(cosa.numeroDeSerie.toString())
        campoFecha.text = cosa.fechaDeCreacion.toString()
        return vista
    }
    companion object {
        fun nuevaInstancia(unaCosa: Cosa): CosaFragment {
            val argumentos = Bundle().apply {
                putParcelable("COSA_RECIBIDA", unaCosa)
            }

            return CosaFragment().apply {
                arguments = argumentos
            }
        }
    }
}