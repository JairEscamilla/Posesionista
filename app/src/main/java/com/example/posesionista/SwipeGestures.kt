package com.example.posesionista

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.content.Context

import androidx.core.content.ContextCompat

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator



// Creo una clase abstracta que ayudara a detectar los swipes del usuario en pantalla
abstract class SwipeGestures(context : Context) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
    ItemTouchHelper.LEFT) {
    private val deleteColor = ContextCompat.getColor(context, R.color.deletecolor) // SETEO EL COLOR DE FONDO AL ELIMINAR
    private val deleteIcon = R.drawable.ic_menu_delete // SETEO EL ICONO A USAR AL ELIMINAR uUNA COSA

    // Sobreescribo el metodo de onChildDraw
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Hago uso de la libreria instalada para setear los efectos al hacer swipe
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}