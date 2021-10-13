package com.example.posesionista

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "TablaDeCosasFragment"
class TablaDeCosasFragment: Fragment() {

    private lateinit var cosaRecyclerView: RecyclerView
    private var adaptador: CosaAdapter? = null
    private var callbackInterfaz: InterfazTablaDeCosas? = null

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

    private fun actualizaUi() {
        val inventario = tablaDeCosasViewModel.inventario
        adaptador = CosaAdapter(inventario)
        cosaRecyclerView.adapter = adaptador
    }

    private val tablaDeCosasViewModel: TablaDeCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaDeCosasViewModel::class.java)
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
        actualizaUi()
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
        private lateinit var cosa: Cosa

        fun binding(cosa: Cosa) {
            this.cosa = cosa
            nombreTextView.text = cosa.nombreDeCosa
            precioTextView.text = "$" + cosa.valorEnPesos.toString()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Toast.makeText(context, "${nombreTextView.text} fue seleccionada", Toast.LENGTH_SHORT).show()
            callbackInterfaz?.onCosasSeleccionada(cosa)
        }
    }

    private inner class CosaAdapter(var inventario: List<Cosa>): RecyclerView.Adapter<cosaHolder>() {
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
    }
}