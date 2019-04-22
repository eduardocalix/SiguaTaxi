package com.example.siguataxi


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.RatingBar
import android.widget.Toast
import com.example.siguataxi.Forma.MensajeChatClase
import com.example.siguataxi.Forma.Usuario
import com.example.siguataxi.Mensajes.NuevoMensajeActivity
import com.example.siguataxi.vistas.ChatDeItem
import com.example.siguataxi.vistas.ChatParaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.actualizar_puntuacion.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {




    /**
     * Estableciendo Adaptador
     * Declaracion de variable paraUsuario, que se utilizara
     * para comonunicacion entre clases
     */
    val adaptador = GroupAdapter<ViewHolder>()
    var paraUsuario: Usuario? = null
    lateinit var tipo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //val objetoIntent: Intent =intent
       // tipo=objetoIntent.getStringExtra("tipoU")

        rvRegistroChat.adapter = adaptador
        paraUsuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        //supportActionBar?.title = paraUsuario?.nombreUsuario
        tvUser.text = paraUsuario?.nombreUsuario
//        if(tipo.toInt()==1){
//         val b:View= findViewById(R.id.btnCalificar)
//            b.setVisibility(View.GONE)
//        }else{
//            val b:View= findViewById(R.id.btnCalificar)
//            b.setVisibility(View.VISIBLE)
//        }
        listaMensajes()
        btnCalificar.setOnClickListener {
            MostrarActualizar()
        }

        btnEnviar_registro_chat.setOnClickListener {
            val date = getCurrentDateTime()
            if(txtEscribirMensaje_registro_chat.text.isEmpty()){
                Toast.makeText(this, "No puedes enviar mensajes en blanco!!!", Toast.LENGTH_SHORT).show()

            }else {
                realizarEnvioMensajes(date)
            }
        }
    }
     fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    private fun listaMensajes() {
        val deId = FirebaseAuth.getInstance().uid
        val paraId = paraUsuario?.idUsuario
        val ref = FirebaseDatabase.getInstance().getReference("usuario-mensajes/$deId/$paraId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val mensajeChat = p0.getValue(MensajeChatClase::class.java)

                if (mensajeChat != null) {
                   // val d = Log.d(TAG, mensajeChat.texto)

                    if (mensajeChat.deId == FirebaseAuth.getInstance().uid) {
                        val usuarioActual = ContenedorMensajesActivity.usuarioActual?: return
                        adaptador.add(ChatDeItem(mensajeChat.texto, usuarioActual,mensajeChat.tiempo))
                    } else {
                        adaptador.add(ChatParaItem(mensajeChat.texto, paraUsuario!!,mensajeChat.tiempo))
                    }
                }

                rvRegistroChat.scrollToPosition(adaptador.itemCount - 1)
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })

    }

    private fun realizarEnvioMensajes(date:Date) {
        // Como enviar mensajes a Firebase...
        val texto = txtEscribirMensaje_registro_chat.text.toString()

        val dateInString = date.toString("HH:mm:ss")
        val deId = FirebaseAuth.getInstance().uid
        val paraUsuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        val paraId = paraUsuario.idUsuario

        if (deId == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/usuario-mensajes/$deId/$paraId").push()

        val paraRef = FirebaseDatabase.getInstance().getReference("/usuario-mensajes/$paraId/$deId").push()

        val mensajeChat = MensajeChatClase(ref.key!!, texto, deId, paraId, dateInString)

        ref.setValue(mensajeChat)
            .addOnSuccessListener {
                txtEscribirMensaje_registro_chat.text.clear()
                rvRegistroChat.scrollToPosition(adaptador.itemCount - 1)
            }

        paraRef.setValue(mensajeChat)

        val ultimosMensajesRef = FirebaseDatabase.getInstance().getReference("ultimos-mensajes/$deId/$paraId")
        ultimosMensajesRef.setValue(mensajeChat)

        val ultimosMensajesParaRef = FirebaseDatabase.getInstance().getReference("ultimos-mensajes/$paraId/$deId")
        ultimosMensajesParaRef.setValue(mensajeChat)
    }
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("Está seguro que desea salir de este chat?")
            .setCancelable(false)
            .setPositiveButton(
                "Si"
            ) { _, _ -> this.finish() }
            .setNegativeButton("No", null)
            .show()
    }

    fun MostrarActualizar(){
        val usuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        Toast.makeText(this, "Usuario Modificado", Toast.LENGTH_SHORT).show()

        val mostrar= AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view=inflater.inflate(R.layout.actualizar_puntuacion,null)
        view.tvUsuarioActualizar?.text= usuario?.nombreUsuario

        view.rbPuntuacionActualizar?.rating=usuario.rating.toFloat()

        Picasso.get()?.load(usuario?.imagenPerfil)?.into(view.img_actualizar)
        val puntuacion=view.findViewById<RatingBar>(R.id.rbPuntuacionActualizar)
        mostrar.setView(view)
        mostrar.setPositiveButton( "Actualizar"){p0,p1->
            val ref= FirebaseDatabase.getInstance().getReference("/infoUsuarios/")
            val usuarioActual =Usuario(usuario.idUsuario,usuario.nombreUsuario,usuario.correo,usuario.telefono,1,usuario.imagenPerfil,usuario.marca,usuario.modelo,usuario.numeroPlaca,usuario.numeroTaxi,puntuacion.rating.toInt())
            ref.child(usuario.idUsuario).setValue(usuarioActual)
            Toast.makeText(this, "Usuario Modificado", Toast.LENGTH_SHORT).show()

        }

        mostrar.setNegativeButton("No"){p0,p1->}
        val alerta=mostrar.create()
        alerta.show()


    }
}
