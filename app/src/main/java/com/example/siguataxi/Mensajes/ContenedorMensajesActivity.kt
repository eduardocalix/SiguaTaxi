package com.example.siguataxi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.siguataxi.Forma.MensajeChatClase
import com.example.siguataxi.Forma.Usuario
import com.example.siguataxi.Mensajes.NuevoMensajeActivity
import com.example.siguataxi.vistas.UltimosMensajesLista
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_contenedor_mensajes.*

class ContenedorMensajesActivity : AppCompatActivity() {

    companion object {
        var usuarioActual: Usuario? = null
        val TAG = "UltimosMensajes"
    }

    val adaptador = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contenedor_mensajes)

        recyclerview_ultimo_mensaje.adapter = adaptador
        recyclerview_ultimo_mensaje.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Seleccionar item con el adaptador
        adaptador.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(this, ChatActivity::class.java)


            val lista = item as UltimosMensajesLista
            intent.putExtra(NuevoMensajeActivity.USER_kEY, lista.chatAmigo)
            startActivity(intent)
        }

        listaUltimosMensajes()
        buscarUsuario()
        verificarInicioSesionUsuario()
        actualizarMensajes()
    }

    val mapaUltimosMensajes = HashMap<String, MensajeChatClase>()

    private fun actualizarMensajes() {
        val adaptador = GroupAdapter<ViewHolder>()
        adaptador.clear()
        mapaUltimosMensajes.values.forEach {
            adaptador.add(UltimosMensajesLista(it))
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
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(MensajeChatClase::class.java) ?: return
                mapaUltimosMensajes[p0.key!!] = chatMessage
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
            val intent = Intent(this, UsuariosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

 override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_nuevoMensaje -> {
                //val intent = Intent(this, NuevoMensajeActivity::class.java)
                startActivity(Intent(this, NuevoMensajeActivity::class.java))
            }
            R.id.menu_cerrarSesion -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
