package com.example.siguataxi

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class Adaptador (var context: Context, var items: ArrayList<Conductores>, var listener: ClickLista, var longClickListener: LongClickListener): RecyclerView.Adapter<Adaptador.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Adaptador.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.conductore_lista, parent, false)

        return ViewHolder(view, listener, longClickListener)
    }

    override fun onBindViewHolder(holder: Adaptador.ViewHolder, position: Int) {
        val item = items.get(position)
        holder.foto.setImageResource(item.foto)
        holder.nombre.text = item.nombre
        holder.telefono.text = item.telefono
        holder.NumeroTaxi.text = item.NumeroTaxi
        holder.NumeroPlaca.text=item.NumeroPlaca
        holder.rating.rating = item.rating.toFloat()
    }

    class ViewHolder(var view: View, var listener: ClickLista, var longClickListener: LongClickListener): RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        override fun onClick(v: View?) {
            listener.onClick(view, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            longClickListener.longClickListener(view, adapterPosition)

            return true
        }

        val foto = view.findViewById<ImageView>(R.id.ivTaxista)
        val nombre = view.findViewById<TextView>(R.id.tvNombre)
        val telefono = view.findViewById<TextView>(R.id.tvTelefono)
        val NumeroTaxi = view.findViewById<TextView>(R.id.tvNumeroTaxi)
        val NumeroPlaca = view.findViewById<TextView>(R.id.tvNumeroPlaca)

        val rating = view.findViewById<RatingBar>(R.id.rbPuntuacion)

        init {
            view.setOnClickListener(this)
        }
    }
}