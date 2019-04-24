package com.example.siguataxi

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.siguataxi.Forma.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.fragment_blank.*



class BlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     buscarUsuario()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }
    val usuari:String="Taxista"
    val tipo:String="Usuario"
    private fun buscarUsuario() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {


                //MenuTaxiActivity.usuarioActual = p0.getValue(Usuario::class.java)
                val nombreUsuario = p0.getValue(Usuario::class.java)
                if (nombreUsuario != null) {

                    //  Log.d("UltimosMensajes", "Usuario Actual ${MenuTaxiActivity.usuarioActual?.imagenPerfil}")
                    tvnombreMenu.text = nombreUsuario.nombreUsuario
                    if(nombreUsuario.tipo==1){
                        tvtipoMenu.text= usuari
                    }else {
                        tvtipoMenu.text = tipo
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
