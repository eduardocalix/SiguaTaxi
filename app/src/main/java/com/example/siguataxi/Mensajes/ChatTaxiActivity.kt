package com.example.siguataxi.Mensajes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.siguataxi.ContenedorMensajesActivity
import com.example.siguataxi.Forma.MensajeChatClase
import com.example.siguataxi.Forma.Usuario
import com.example.siguataxi.MenuTaxiActivity
import com.example.siguataxi.R
import com.example.siguataxi.vistas.ChatDeItem
import com.example.siguataxi.vistas.ChatParaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_chat_taxi.*
import java.text.SimpleDateFormat
import java.util.*

class ChatTaxiActivity : AppCompatActivity()  {



    companion object {
        val TAG = "RegistroChat"
    }

    /**
     * Estableciendo Adaptador
     * Declaracion de variable paraUsuario, que se utilizara
     * para comonunicacion entre clases
     */
    val adaptador = GroupAdapter<ViewHolder>()
    var paraUsuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_taxi)

        rvRegistroChatTaxi.adapter = adaptador
        paraUsuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        supportActionBar?.title = paraUsuario?.nombreUsuario

        listaMensajes()

        btnEnviar_registro_chat_taxi.setOnClickListener {
            realizarEnvioMensajes()
        }
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    private lateinit var ValorHora:String
    private fun listaMensajes() {
        val deId = FirebaseAuth.getInstance().uid
        val paraId = paraUsuario?.idUsuario
        val ref = FirebaseDatabase.getInstance().getReference("usuario-mensajes/$deId/$paraId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val mensajeChat = p0.getValue(MensajeChatClase::class.java)

                if (mensajeChat != null) {
                    Log.d(TAG, mensajeChat.texto)
                    var date = getCurrentDateTime()
                    var dateInString = date.toString("HH:mm:ss")
                    ValorHora=dateInString
                    if (mensajeChat.deId == FirebaseAuth.getInstance().uid) {
                        val usuarioActual = ContenedorMensajesActivity.usuarioActual?: return
                        adaptador.add(ChatDeItem(mensajeChat.texto, usuarioActual,dateInString))
                    } else {
                        adaptador.add(ChatParaItem(mensajeChat.texto, paraUsuario!!,dateInString))
                    }
                }

                rvRegistroChatTaxi.scrollToPosition(adaptador.itemCount - 1)
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })

    }

    private fun realizarEnvioMensajes() {
        // Como enviar mensajes a Firebase...
        val texto = txtEscribirMensaje_registro_chat_taxi.text.toString()

        val deId = FirebaseAuth.getInstance().uid
        val usuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        val paraId = usuario.idUsuario

        if (deId == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/usuario-mensajes/$deId/$paraId").push()

        val paraRef = FirebaseDatabase.getInstance().getReference("/usuario-mensajes/$paraId/$deId").push()

        val mensajeChat = MensajeChatClase(ref.key!!, texto, deId, paraId, ValorHora)
        ref.setValue(mensajeChat)
            .addOnSuccessListener {
                txtEscribirMensaje_registro_chat_taxi.text.clear()
                rvRegistroChatTaxi.scrollToPosition(adaptador.itemCount - 1)
            }

        paraRef.setValue(mensajeChat)

        val ultimosMensajesRef = FirebaseDatabase.getInstance().getReference("ultimos-mensajes/$deId/$paraId")
        ultimosMensajesRef.setValue(mensajeChat)

        val ultimosMensajesParaRef = FirebaseDatabase.getInstance().getReference("ultimos-mensajes/$paraId/$deId")
        ultimosMensajesParaRef.setValue(mensajeChat)
    }
}
