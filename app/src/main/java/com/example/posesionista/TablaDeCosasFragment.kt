package com.example.posesionista

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


class TablaDeCosasFragment: Fragment() {
    // Variables iniciales necesarias para manejar la tabla de cosas
    private lateinit var cosaRecyclerView: RecyclerView
    private lateinit var sectionRecyclerView: RecyclerView
    private var adaptador: CosaAdapter? = null
    private var adaptadorSections: SectionsAdapter ? = null
    private var callbackInterfaz: InterfazTablaDeCosas? = null
    // Cargamos el view model
    private val tablaDeCosasViewModel: TablaDeCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaDeCosasViewModel::class.java)
    }

    // Generamos una interface para manejar las cosas seleccionadas
    interface InterfazTablaDeCosas {
        fun onCosasSeleccionada(unaCosa: Cosa, tablaCosas: TablaDeCosasViewModel, editing: Boolean)
    }

    override fun onStart() {
        super.onStart()
        val bar = activity as AppCompatActivity
        bar.supportActionBar?.setTitle(R.string.app_name)
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
        adaptadorSections = SectionsAdapter(tablaDeCosasViewModel.listOfSections)
        sectionRecyclerView.adapter = adaptadorSections

        /*val inventario = tablaDeCosasViewModel.inventario // Obtengo el inventario
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
                val fromPos = viewHolder.absoluteAdapterPosition
                val toPos = target.absoluteAdapterPosition

                Collections.swap(inventario, fromPos, toPos)
                adaptador?.notifyItemMoved(fromPos, toPos)

                return false
            }
        }
        val touchHelper = ItemTouchHelper(swipegestures)
        touchHelper.attachToRecyclerView(cosaRecyclerView)
        cosaRecyclerView.adapter = adaptador*/
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Generamos el Recycler view y después actualizamos el UI
        val vista = inflater.inflate(R.layout.lista_cosas_fragment, container, false)
        sectionRecyclerView = vista.findViewById(R.id.cosa_recycler_view) as RecyclerView
        sectionRecyclerView.layoutManager = LinearLayoutManager(context)
        actualizaUi(context as Context)
        return vista
    }

    private inner class CosaHolder(vista: View): RecyclerView.ViewHolder(vista), View.OnClickListener {
        private val nombreTextView: TextView = itemView.findViewById(R.id.label_nombre)
        private val precioTextView: TextView = itemView.findViewById(R.id.label_precio)
        private val serieTextView: TextView = itemView.findViewById(R.id.label_serie)
        private lateinit var cosa: Cosa

        @SuppressLint("SetTextI18n")
        fun binding(cosa: Cosa) {
            // Seteamos a la vista los valos de la cosa
            this.cosa = cosa
            nombreTextView.text = cosa.nombreDeCosa
            precioTextView.text = "$" + cosa.valorEnPesos.toString()
            serieTextView.text = cosa.numeroDeSerie
        }



        init {
            itemView.setOnClickListener(this) // Seteamos el clicklistener al item
        }

        override fun onClick(v: View?) {
            callbackInterfaz?.onCosasSeleccionada(cosa, tablaDeCosasViewModel, true) // Ejecutamos la funcion de oncosa seleccionada que viene de la interfaz
        }
    }


    private inner class SectionsAdapter(var listOfSections: ArrayList<Sections>): RecyclerView.Adapter<SectionsAdapter.DataViewHolder>() {

        inner class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private val sectionNameTV: TextView = itemView.findViewById(R.id.section_title)
            private val cosasRV: RecyclerView = itemView.findViewById(R.id.child_recycler_view)
            fun bind(section: Sections) {
                sectionNameTV.text = section.section
                val cosaAdapter = CosaAdapter(section.list)
                cosasRV.layoutManager = LinearLayoutManager(context)
                cosasRV.adapter = cosaAdapter
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val holder = LayoutInflater.from(parent.context).inflate(R.layout.sections_layout, parent, false)
            return DataViewHolder(holder)
        }

        override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
            holder.bind(listOfSections[position])
        }

        override fun getItemCount(): Int {
            return listOfSections.size
        }
    }

    // Creamos el adapter
    private inner class CosaAdapter(var inventario: ArrayList<Cosa>): RecyclerView.Adapter<CosaHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CosaHolder {
            val holder = layoutInflater.inflate(R.layout.cosa_layout, parent, false) // Inflamos el layout
            return CosaHolder(holder) // Regresamos el holder
        }

        override fun getItemCount(): Int {  // Regresamos el Item count como el tamaño del inventario
            return inventario.size
        }

        override fun onBindViewHolder(holder: CosaHolder, position: Int) { // Obtenemos la la cosa actual y se la bindeamos al holder
            val cosa = inventario[position]
            holder.binding(cosa)
            holder.itemView.setBackgroundColor(getFragmentColor(cosa.valorEnPesos))
        }
        // Funcion para obtener el color de fondo de los precios de acuerdo al rango en el cual se encuentran
        fun getFragmentColor(price: Int): Int {

            val priceColor = when(price) {
                in 0..100 -> Color.BLACK
                in 100..200 -> Color.BLUE
                in 200..300 -> Color.CYAN
                in 300..400 -> Color.DKGRAY
                in 400..500 -> Color.GRAY
                in 500..600 -> Color.rgb(153, 206, 244)
                in 600..700 -> Color.MAGENTA
                in 700..800 -> Color.RED
                in 800..900 -> Color.rgb(152, 202, 63)
                in 900..1000 -> Color.rgb(32, 41, 66)
                else -> Color.rgb(54, 179, 126)
            }

            return  priceColor
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteItem(position: Int) { // Funcion para eliminar un item
            val builder = AlertDialog.Builder(activity) // Genero al alert para confirmar que se quiere eliminar el item
            builder.setTitle(R.string.dialog_title)
            builder.setMessage(R.string.dialog_delete_item)
            builder.setPositiveButton(R.string.si) { dialog, _ -> // En caso de que la respuesta sea positiva, eliminamos el item
                inventario.removeAt(position)
                notifyDataSetChanged()
                dialog.cancel()
            }

            builder.setNegativeButton(R.string.cancel) { dialog, _ -> // En caso negativo, se cierra el dialogo
                notifyDataSetChanged()
                dialog.cancel()
            }
            builder.setOnDismissListener {
                notifyDataSetChanged()
            }
            val alert: AlertDialog = builder.create()
            alert.show() // Muestro el alert
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_tabla_de_cosas, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nueva_cosa -> {
                val nuevaCosa = Cosa()
                callbackInterfaz?.onCosasSeleccionada(nuevaCosa, tablaDeCosasViewModel, false)
                true
            } else -> return super.onOptionsItemSelected(item)
        }

    }
}