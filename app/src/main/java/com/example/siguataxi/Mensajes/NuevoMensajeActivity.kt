package com.example.siguataxi.Mensajes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.siguataxi.ChatActivity
import com.example.siguataxi.R
import com.example.siguataxi.Forma.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_nuevo_mensaje.*
import kotlinx.android.synthetic.main.nuevo_mensaje_lista_usuario.view.*

class NuevoMensajeActivity : AppCompatActivity() {
    companion object {
        val USER_kEY = "USER_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_mensaje)
        // Editando barra de titulo
        supportActionBar?.title = "Seleccionar Usuario"
        buscarUsuarios()
    }
    /**
     * Realizar busqueda de usuarios en la base de datos
     */
    private fun buscarUsuarios() {
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios")
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
                    startActivity(intent)
                    finish()
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
        Picasso.get().load(usuario.imagenPerfil).into(viewHolder.itemView.img_nuevo_mensaje)
    }

    override fun getLayout(): Int {
        return R.layout.nuevo_mensaje_lista_usuario
    }
}

