package com.example.posesionista

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.util.*


class CosaFragment: Fragment() {
    private lateinit var cosa: Cosa
    private lateinit var campoNombre: EditText
    private lateinit var campoPrecio: EditText
    private lateinit var campoSerie: EditText
    private lateinit var campoFecha: TextView
    private lateinit var vistaParaFoto: ImageView
    private lateinit var botonDeCamara: ImageButton
    private lateinit var archivoDeFoto: File
    private var respuestaCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            //val datos = resultado.data
            //vistaParaFoto.setImageBitmap(datos?.extras?.get("data") as Bitmap)
            vistaParaFoto.setImageBitmap(BitmapFactory.decodeFile(archivoDeFoto.absolutePath))
        }
    }

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
                    cosa.nombreDeCosa = s.toString()
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
        val barraDeActividad = activity as AppCompatActivity
        barraDeActividad.supportActionBar?.setTitle(R.string.detalle_cosa)
        botonDeCamara.apply {
            setOnClickListener{
                val intentTomarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                archivoDeFoto = obtenerArchivoDeFoto("${cosa.idCosa}.jpg")
                val fileProvider = FileProvider.getUriForFile(context, "com.example.posesionista.fileprovider", archivoDeFoto)
                intentTomarFoto.putExtra(
                    MediaStore.EXTRA_OUTPUT, fileProvider
                )
                try {
                    respuestaCamara.launch(intentTomarFoto)

                }catch (e: ActivityNotFoundException) {

                }
            }
        }
    }

    private fun obtenerArchivoDeFoto(nombreDeArchivo: String): File {
        val pathParaFotos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(pathParaFotos, nombreDeArchivo)
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
        vistaParaFoto = vista.findViewById(R.id.fotoDeCosa)
        botonDeCamara = vista.findViewById(R.id.botonDeCamara)

        archivoDeFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${cosa.idCosa}.jpg")
        Log.d("FOTO", "${archivoDeFoto.totalSpace}")
        if(archivoDeFoto.totalSpace == 0L) {
            vistaParaFoto.setImageResource(R.drawable.placeholder)
        }else {
            vistaParaFoto.setImageBitmap(BitmapFactory.decodeFile(archivoDeFoto.absolutePath))
        }



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
                            val newDate = "${dayOfMonth}-${monthOfYear + 1}-${yearN}"
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