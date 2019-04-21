package com.example.siguataxi.vistas

import com.example.siguataxi.Forma.MensajeChatClase
import com.example.siguataxi.R
import com.example.siguataxi.Forma.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.ultimos_mensajes_lista.view.*

class UltimosMensajesLista( val mensajesChat: MensajeChatClase): Item<ViewHolder>() {
  var chatAmigo: Usuario? = null

  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.mensaje_textview_ultimos_mensajes.text = mensajesChat.texto
    viewHolder.itemView.tvHorapara2.text=mensajesChat.tiempo


    val chatAmigoId: String
    if (mensajesChat.deId == FirebaseAuth.getInstance().uid) {
      chatAmigoId = mensajesChat.paraId
    } else {
      chatAmigoId = mensajesChat.deId
    }

    val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$chatAmigoId")
    ref.addListenerForSingleValueEvent(object: ValueEventListener {
      override fun onDataChange(p0: DataSnapshot) {
        chatAmigo = p0.getValue(Usuario::class.java)

        viewHolder.itemView.usuario_textview_ultimos_mensajes.text = chatAmigo?.nombreUsuario

        val targetImageView = viewHolder.itemView.img_ultimos_mensajes
        Picasso.get().load(chatAmigo?.imagenPerfil).into(targetImageView)
      }

      override fun onCancelled(p0: DatabaseError) {}
    })
  }

  override fun getLayout(): Int {
    return R.layout.ultimos_mensajes_lista
  }
}