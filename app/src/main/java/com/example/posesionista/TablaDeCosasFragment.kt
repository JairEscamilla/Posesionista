package com.example.posesionista

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TablaDeCosasFragment"
class TablaDeCosasFragment: Fragment() {

    private lateinit var cosaRecyclerView: RecyclerView
    private var adaptador: CosaAdapter? = null
    private var callbackInterfaz: InterfazTablaDeCosas? = null
    private val tablaDeCosasViewModel: TablaDeCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaDeCosasViewModel::class.java)
    }

    interface InterfazTablaDeCosas {
        fun onCosasSeleccionada(unaCosa: Cosa)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackInterfaz = context as InterfazTablaDeCosas?
    }

    override fun onDetach() {
        super.onDetach()
        callbackInterfaz = null
    }

    private fun actualizaUi(context: Context) {
        val inventario = tablaDeCosasViewModel.inventario
        adaptador = CosaAdapter(inventario)
        val swipegestures = object : SwipeGestures(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT) {
                    adaptador?.deleteItem(viewHolder.absoluteAdapterPosition)
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
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
            this.cosa = cosa
            val priceColor = getPriceColor(cosa.valorEnPesos)
            nombreTextView.text = cosa.nombreDeCosa
            precioTextView.text = "$" + cosa.valorEnPesos.toString()
            serieTextView.text = cosa.numeroDeSerie
            precioTextView.setTextColor(priceColor)
        }

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
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Toast.makeText(context, "${nombreTextView.text} fue seleccionada", Toast.LENGTH_SHORT).show()
            callbackInterfaz?.onCosasSeleccionada(cosa)
        }
    }

    private inner class CosaAdapter(var inventario: ArrayList<Cosa>): RecyclerView.Adapter<cosaHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cosaHolder {
            val holder = layoutInflater.inflate(R.layout.cosa_layout, parent, false)
            return cosaHolder(holder)
        }

        override fun getItemCount(): Int {
            return inventario.size
        }

        override fun onBindViewHolder(holder: cosaHolder, position: Int) {
            val cosa = inventario[position]
           /* holder.apply {
                nombreTextView.text = cosa.nombreDeCosa
                precioTextView.text = "$${cosa.valorEnPesos}"
            }*/
            holder.binding(cosa)
        }

        fun deleteItem(position: Int) {
            inventario.removeAt(position)
            notifyDataSetChanged()
        }
    }
}