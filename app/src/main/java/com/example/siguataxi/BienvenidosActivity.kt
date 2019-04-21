package com.example.siguataxi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.siguataxi.Forma.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BienvenidosActivity : AppCompatActivity() {

    private var tipo:Int=0
    private lateinit var  nombre:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenidos)
        buscarUsuario()

        if(tipo==0){

            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
            Toast.makeText(this, "Bienvenido  $nombre", Toast.LENGTH_SHORT).show()
            this.finish()

        }else{
            /*val intent = Intent(this,MenuTaxiActivity::class.java)
            startActivity(intent)*/
             val intent = Intent(this, MenuTaxiActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
            Toast.makeText(this, "Bienvenido  $nombre", Toast.LENGTH_SHORT).show()
            this.finish()


        }
    }

    private fun buscarUsuario() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {


               // MenuTaxiActivity.usuarioActual = p0.getValue(Usuario::class.java)
                val nombreUsuario = p0.getValue(Usuario::class.java)
                if (nombreUsuario != null) {

                    tipo ==nombreUsuario.tipo
                    nombre=nombreUsuario.nombreUsuario

                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

}
