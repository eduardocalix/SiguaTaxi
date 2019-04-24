package com.example.siguataxi.Mensajes

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.siguataxi.ChatActivity
import com.example.siguataxi.R
import com.example.siguataxi.Forma.Usuario
import com.example.siguataxi.MenuTaxiActivity.Companion.usuarioActual
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_nuevo_mensaje.*
import kotlinx.android.synthetic.main.nuevo_mensaje_lista_usuario.*
import kotlinx.android.synthetic.main.nuevo_mensaje_lista_usuario.view.*

class NuevoMensajeActivity : Fragment() {
    companion object {
        val USER_kEY = "USER_KEY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        buscarUsuarios()

        return inflater.inflate(R.layout.activity_nuevo_mensaje,container,false)
   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        // Editando barra de titulo
        supportActionBar?.title = "Seleccionar Taxista"*/

    }
    /**
     * Realizar busqueda de usuarios en la base de datos
     */
    private fun buscarUsuarios() {
        val valor=1
        //val reftaxi=FirebaseDatabase.getInstance().getReference("/infoTaxi")
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios")
            .orderByChild("/tipo").equalTo(valor.toDouble())
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adaptador = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    //Log.d("NuevoMensaje", it.toString())
                    val nombreUsuario = it.getValue(Usuario::class.java)
                    if (nombreUsuario != null){
                        adaptador.add(ItemsUsuarios(nombreUsuario))
                    }
                }

                adaptador.setOnItemClickListener { item, view ->
                    val itemsUsuarios = item as ItemsUsuarios
                    val intent = Intent(view.context, ChatActivity::class.java)
                    intent.putExtra(USER_kEY, itemsUsuarios.usuario)
                    //intent.putExtra("tipoU", usuarioActual?.tipo)

                    startActivity(intent)

                }
                rvNuevosMensajes.adapter = adaptador
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}




/**
 * Cargar Recycler View con los usuarios
 */
class ItemsUsuarios(val usuario: Usuario): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_nuevo_mensaje_lista_usuario.text = usuario.nombreUsuario
        viewHolder.itemView.tvTelefono.text=usuario.telefono
        viewHolder.itemView.tvNumeroPlaca.text=usuario.numeroPlaca
        viewHolder.itemView.tvNumeroTaxi.text=usuario.numeroTaxi
        viewHolder.itemView.rbPuntuacion.rating=usuario.rating.toFloat()

        Picasso.get().load(usuario.imagenPerfil).into(viewHolder.itemView.img_nuevo_mensaje)
    }

    override fun getLayout(): Int {
        return R.layout.nuevo_mensaje_lista_usuario
    }
}

