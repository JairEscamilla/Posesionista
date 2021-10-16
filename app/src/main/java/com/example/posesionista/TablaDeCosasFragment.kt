package com.example.posesionista

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TablaDeCosasFragment"
class TablaDeCosasFragment: Fragment() {
    // Variables iniciales necesarias para manejar la tabla de cosas
    private lateinit var cosaRecyclerView: RecyclerView
    private var adaptador: CosaAdapter? = null
    private var callbackInterfaz: InterfazTablaDeCosas? = null
    // Cargamos el view model
    private val tablaDeCosasViewModel: TablaDeCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaDeCosasViewModel::class.java)
    }

    // Generamos una interface para manejar las cosas seleccionadas
    interface InterfazTablaDeCosas {
        fun onCosasSeleccionada(unaCosa: Cosa)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackInterfaz = context as InterfazTablaDeCosas?
        // seteamos el contexto al callback de la interfaz
    }

    override fun onDetach() {
        super.onDetach()
        callbackInterfaz = null
        // Se hace null el callback para evitar fugas en memoria
    }

    // Funcion que actualiza la UI y recibe el contexto de la aplicacion
    private fun actualizaUi(context: Context) {
        val inventario = tablaDeCosasViewModel.inventario // Obtengo el inventario
        adaptador = CosaAdapter(inventario) // Obtengo el adaptador del recycler view
        val swipegestures = object : SwipeGestures(context) { //  Genero los listeners para cuando se detecte el swipe del usuario
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT) {
                    adaptador?.deleteItem(viewHolder.absoluteAdapterPosition) // Si se hace swipe hacia la izquierda, entonces elimino el item
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Al moverse verticalmente, se mueve la posicion del item seleccionado
                val from_pos = viewHolder.absoluteAdapterPosition
                val to_pos = target.absoluteAdapterPosition

                Collections.swap(inventario, from_pos, to_pos)
                adaptador?.notifyItemMoved(from_pos, to_pos)

                return false
            }
        }
        val touchHelper = ItemTouchHelper(swipegestures)
        touchHelper.attachToRecyclerView(cosaRecyclerView)
        cosaRecyclerView.adapter = adaptador
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Generamos el Recycler view y después actualizamos el UI
        val vista = inflater.inflate(R.layout.lista_cosas_fragment, container, false)
        cosaRecyclerView = vista.findViewById(R.id.cosa_recycler_view) as RecyclerView
        cosaRecyclerView.layoutManager = LinearLayoutManager(context)
        actualizaUi(context!!)
        return vista
    }

    companion object {
        fun nuevaInstancia(): TablaDeCosasFragment {
            return TablaDeCosasFragment()
        }
    }

    private inner class cosaHolder(vista: View): RecyclerView.ViewHolder(vista), View.OnClickListener {
        private val nombreTextView: TextView = itemView.findViewById(R.id.label_nombre)
        private val precioTextView: TextView = itemView.findViewById(R.id.label_precio)
        private val serieTextView: TextView = itemView.findViewById(R.id.label_serie)
        private lateinit var cosa: Cosa

        fun binding(cosa: Cosa) {
            // Seteamos a la vista los valos de la cosa
            this.cosa = cosa
            val priceColor = getPriceColor(cosa.valorEnPesos)
            nombreTextView.text = cosa.nombreDeCosa
            precioTextView.text = "$" + cosa.valorEnPesos.toString()
            serieTextView.text = cosa.numeroDeSerie
            precioTextView.setTextColor(priceColor)
        }

        // Funcion para obtener el color de fondo de los precios de acuerdo al rango en el cual se encuentran
        fun getPriceColor(price: Int): Int {
            var priceColor: Int
            when(price) {
                in 0..100 -> priceColor = Color.BLACK
                in 100..200 -> priceColor = Color.BLUE
                in 200..300 -> priceColor = Color.CYAN
                in 300..400 -> priceColor = Color.DKGRAY
                in 400..500 -> priceColor = Color.GRAY
                in 500..600 -> priceColor = Color.rgb(153, 206, 244)
                in 600..700 -> priceColor = Color.MAGENTA
                in 700..800 -> priceColor = Color.RED
                in 800..900 -> priceColor = Color.rgb(152, 202, 63)
                in 900..1000 -> priceColor = Color.rgb(32, 41, 66)
                else -> priceColor = Color.rgb(54, 179, 126)
            }

            return  priceColor
        }

        init {
            itemView.setOnClickListener(this) // Seteamos el clicklistener al item
        }

        override fun onClick(v: View?) {
            callbackInterfaz?.onCosasSeleccionada(cosa) // Ejecutamos la funcion de oncosa seleccionada que viene de la interfaz
        }
    }

    // Creamos el adapter
    private inner class CosaAdapter(var inventario: ArrayList<Cosa>): RecyclerView.Adapter<cosaHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cosaHolder {
            val holder = layoutInflater.inflate(R.layout.cosa_layout, parent, false) // Inflamos el layout
            return cosaHolder(holder) // Regresamos el holder
        }

        override fun getItemCount(): Int {  // Regresamos el Item count como el tamaño del inventario
            return inventario.size
        }

        override fun onBindViewHolder(holder: cosaHolder, position: Int) { // Obtenemos la la cosa actual y se la bindeamos al holder
            val cosa = inventario[position]
            holder.binding(cosa)
        }

        fun deleteItem(position: Int) { // Funcion para eliminar un item
            var builder = AlertDialog.Builder(activity) // Genero al alert para confirmar que se quiere eliminar el item
            builder.setTitle(R.string.dialog_title)
            builder.setMessage(R.string.dialog_delete_item)
            builder.setPositiveButton(R.string.si, {dialog, _ -> // En caso de que la respuesta sea positiva, eliminamos el item
                inventario.removeAt(position)
                notifyDataSetChanged()
                dialog.cancel()
            })

            builder.setNegativeButton(R.string.cancel, {dialog, _ -> // En caso negativo, se cierra el dialogo
                notifyDataSetChanged()
                dialog.cancel()
            })
            builder.setOnDismissListener {
                notifyDataSetChanged()
            }
            var alert: AlertDialog = builder.create()
            alert.show() // Muestro el alert
        }
    }
}