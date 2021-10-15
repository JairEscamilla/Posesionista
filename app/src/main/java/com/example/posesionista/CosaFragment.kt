package com.example.posesionista

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*


class CosaFragment: Fragment() {
    private lateinit var cosa: Cosa
    private lateinit var campoNombre: EditText
    private lateinit var campoPrecio: EditText
    private lateinit var campoSerie: EditText
    private lateinit var campoFecha: TextView

    // Funcion del ciclo de vida que usamos para obtener los datos de la cosa que estamos recibiendo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosa = Cosa()
        cosa = arguments?.getParcelable("COSA_RECIBIDA")!!
    }



    override fun onStart() {
        super.onStart()
        // Observador para detectar lo0s cambios en los edit text y despues setear los valores alobjeto de cosa
        val observador = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            // Funcion que se ejecuta al cambiar el texto del input
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.hashCode() == campoNombre.text.hashCode()){
                    if (s.toString().isEmpty()){ // Valido que el campo no pueda estar vacio
                        Toast.makeText(activity, "El campo nombre no puede estar vacio", Toast.LENGTH_SHORT).show()
                    }else{
                        cosa.nombreDeCosa = s.toString()
                    }

                }
                if(s.hashCode() == campoPrecio.text.hashCode()){
                    if(s!= null){
                        if(s.isEmpty()){ // Valido el valor de el string del campo precio
                            cosa.valorEnPesos = 0
                        } else{
                            cosa.valorEnPesos = s.toString().toInt()
                        }
                    }

                }
                if(s.hashCode() == campoSerie.text.hashCode()){ // El numero de serio solamente se setea
                    cosa.numeroDeSerie = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        // seteamos el observador como listener a los campos
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
        val button = vista.findViewById<Button>(R.id.pickDateBtn)
        // Se crean las variables para los campos
        campoNombre = vista.findViewById(R.id.nombre_cosa) as EditText
        campoPrecio = vista.findViewById(R.id.precio_cosa) as EditText
        campoSerie = vista.findViewById(R.id.serie_cosa) as EditText
        campoFecha = vista.findViewById(R.id.labelFecha) as TextView
        campoNombre.setText(cosa.nombreDeCosa)
        campoPrecio.setText(cosa.valorEnPesos.toString())
        campoSerie.setText(cosa.numeroDeSerie)
        campoFecha.text = cosa.fechaDeCreacion

        // Seteo un listener para cuando se ocupe abrir el date picker
        button.setOnClickListener {
            // Creo la instancia del calendario y obtengo el dia, mes y anio
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd =
                    activity?.let { it1 ->
                        DatePickerDialog(it1, { _, yearN, monthOfYear, dayOfMonth ->
                            val newDate = "${dayOfMonth}-${monthOfYear}-${yearN}"
                            campoFecha.text = newDate
                            cosa.fechaDeCreacion = newDate

                        }, year, month, day)
                    } // Genero el Date Picker y seteo el valor de la fecha al la propiedad fecha de creación del objeto cosa

            dpd?.datePicker?.maxDate = c.timeInMillis // Seteo la fecha maxima que se puede seleccionar
            dpd?.show() // Muestro el date picker
        }
        return vista
    }

    // Companion object para manejar los argumentos que llegan de Parcellable
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