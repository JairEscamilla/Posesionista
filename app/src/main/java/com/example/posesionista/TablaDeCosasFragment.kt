package com.example.posesionista

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class TablaDeCosasFragment: Fragment() {
    // Variables iniciales necesarias para manejar la tabla de cosas
    private lateinit var sectionRecyclerView: RecyclerView
    private var adaptadorSections: SectionsAdapter ? = null
    private var callbackInterfaz: InterfazTablaDeCosas? = null
    private lateinit var totalThingsTV: TextView
    private lateinit var totalPricesTV: TextView
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
    private fun actualizaUi() {
        adaptadorSections = SectionsAdapter(tablaDeCosasViewModel.listOfSections)
        sectionRecyclerView.adapter = adaptadorSections
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
        totalThingsTV = vista.findViewById(R.id.total_cosas)
        totalPricesTV = vista.findViewById(R.id.total_precios)

        actualizaUi()
        return vista
    }

    private inner class CosaHolder(vista: View): RecyclerView.ViewHolder(vista), View.OnClickListener {
        private val nombreTextView: TextView = itemView.findViewById(R.id.label_nombre)
        private val precioTextView: TextView = itemView.findViewById(R.id.label_precio)
        private val serieTextView: TextView = itemView.findViewById(R.id.label_serie)
        private val imageContainer: ImageView = itemView.findViewById(R.id.cosa_image)
        private lateinit var cosa: Cosa

        @SuppressLint("SetTextI18n")
        fun binding(cosa: Cosa) {
            // Seteamos a la vista los valos de la cosa
            this.cosa = cosa
            nombreTextView.text = cosa.nombreDeCosa
            precioTextView.text = "$" + cosa.valorEnPesos.toString()
            serieTextView.text = cosa.numeroDeSerie
            imageContainer.setImageResource(R.drawable.placeholder)
            val archivoDeFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${cosa.idCosa}.jpg")
            if(archivoDeFoto.totalSpace == 0L) {
                imageContainer.setImageResource(R.drawable.placeholder)
            }else {
                imageContainer.setImageBitmap(BitmapFactory.decodeFile(archivoDeFoto.absolutePath))
            }
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
            fun bind(section: Sections, backgroundColor: Int) {
                sectionNameTV.text = section.section
                val cosaAdapter = CosaAdapter(section.list)
                cosasRV.layoutManager = LinearLayoutManager(context)
                cosasRV.adapter = cosaAdapter
                cosasRV.setBackgroundColor(backgroundColor)
                val swipegestures = object : SwipeGestures(context!!) { //  Genero los listeners para cuando se detecte el swipe del usuario
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        if(direction == ItemTouchHelper.LEFT) {
                            cosaAdapter.deleteItem(viewHolder.absoluteAdapterPosition) // Si se hace swipe hacia la izquierda, entonces elimino el item
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

                        Collections.swap(section.list, fromPos, toPos)
                        cosaAdapter.notifyItemMoved(fromPos, toPos)

                        return false
                    }
                }
                val touchHelper = ItemTouchHelper(swipegestures)
                touchHelper.attachToRecyclerView(cosasRV)
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val holder = LayoutInflater.from(parent.context).inflate(R.layout.sections_layout, parent, false)
            return DataViewHolder(holder)
        }

        override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
            val backgroundColor = getFragmentColor(position)
            holder.bind(listOfSections[position], backgroundColor)
            holder.itemView.setBackgroundColor(backgroundColor)
        }

        override fun getItemCount(): Int {
            return listOfSections.size
        }
        fun getFragmentColor(position: Int): Int {

            val priceColor = when(position) {
                in -1..0 -> Color.BLACK
                in 0..1 -> Color.BLUE
                in 1..2 -> Color.CYAN
                in 2..3 -> Color.DKGRAY
                in 3..4 -> Color.GRAY
                in 4..5 -> Color.rgb(153, 206, 244)
                in 5..6 -> Color.MAGENTA
                in 6..7 -> Color.RED
                in 7..8 -> Color.rgb(152, 202, 63)
                in 8..9 -> Color.rgb(32, 41, 66)
                else -> Color.rgb(54, 179, 126)
            }

            return  priceColor
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
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteItem(position: Int) { // Funcion para eliminar un item
            val builder = AlertDialog.Builder(activity) // Genero al alert para confirmar que se quiere eliminar el item
            builder.setTitle(R.string.dialog_title)
            builder.setMessage(R.string.dialog_delete_item)
            builder.setPositiveButton(R.string.si) { dialog, _ -> // En caso de que la respuesta sea positiva, eliminamos el item
                val currentCosa = inventario[position]
                val archivoDeFoto = obtenerArchivoDeFoto("${currentCosa.idCosa}.jpg")
                archivoDeFoto.delete()
                inventario.removeAt(position)
                updateTotals()
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
        private fun obtenerArchivoDeFoto(nombreDeArchivo: String): File {
            val pathParaFotos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(pathParaFotos, nombreDeArchivo)
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

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        updateTotals()
    }

    fun updateTotals() {
        val totalThings = tablaDeCosasViewModel.getTotalThings()
        val totalPrices: Int = tablaDeCosasViewModel.getTotalPrices()
        "$totalThings cosa(s)".also { totalThingsTV.text = it }
        "$$totalPrices".also { totalPricesTV.text = it }
    }

}