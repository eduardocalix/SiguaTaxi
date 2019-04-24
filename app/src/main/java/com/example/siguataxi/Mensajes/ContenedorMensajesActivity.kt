package com.example.siguataxi


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.example.siguataxi.ChatActivity
import com.example.siguataxi.Forma.MensajeChatClase
import com.example.siguataxi.Forma.Usuario
import com.example.siguataxi.LoginActivity
import com.example.siguataxi.Mensajes.NuevoMensajeActivity
import com.example.siguataxi.UsuariosActivity
import com.example.siguataxi.vistas.UltimosMensajesLista
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_contenedor_mensajes.*
import kotlinx.android.synthetic.main.nuevo_mensaje_lista_usuario.*


class ContenedorMensajesActivity : Fragment() {


    companion object {
        var usuarioActual: Usuario? = null
        val TAG = "UltimosMensajesUsuarios"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)




        // Seleccionar item con el adaptador
        adaptador.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(view.context, ChatActivity::class.java)
            //intent.putExtra("tipoU", usuarioActual?.tipo)

            val lista = item as UltimosMensajesLista
            intent.putExtra(NuevoMensajeActivity.USER_kEY, lista.chatAmigo)
            startActivity(intent)
        }

        listaUltimosMensajes()
        buscarUsuario()
        verificarInicioSesionUsuario()
        Log.d("UltimosMensajes", "Llenar mensaje")

        return inflater.inflate(R.layout.activity_contenedor_mensajes,container,false)

    }
    val adaptador = GroupAdapter<ViewHolder>()

    val mapaUltimosMensajes = HashMap<String, MensajeChatClase>()

    private fun actualizarMensajes() {
        adaptador.clear()
        mapaUltimosMensajes.values.forEach {
            adaptador.add(UltimosMensajesLista(it))
            Log.d("UltimosMensajes", "Actualizar mensaje")

            recyclerview_ultimo_mensaje?.adapter = adaptador
            recyclerview_ultimo_mensaje?.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun listaUltimosMensajes() {
        val deId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/ultimos-mensajes/$deId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val mensajeChat = p0.getValue(MensajeChatClase::class.java) ?: return
                mapaUltimosMensajes[p0.key!!] = mensajeChat
                actualizarMensajes()
                Log.d("UltimosMensajes", "Lista mensaje")

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val mensajesChat = p0.getValue(MensajeChatClase::class.java) ?: return
                mapaUltimosMensajes[p0.key!!] = mensajesChat
                actualizarMensajes()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
            override fun onCancelled(p0: DatabaseError) {}
        })
    }
    private fun buscarUsuario() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                usuarioActual = p0.getValue(Usuario::class.java)
                Log.d("UltimosMensajes", "Usuario Actual ${usuarioActual?.imagenPerfil}")
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun verificarInicioSesionUsuario() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this.context, UsuariosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}
