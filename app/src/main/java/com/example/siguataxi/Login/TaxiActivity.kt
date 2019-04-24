package com.example.siguataxi


import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import android.widget.Toast

import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_taxi.*
import kotlinx.android.synthetic.main.activity_usuarios.*


class TaxiActivity : AppCompatActivity() {

    //La variable fileUri Almacena la foto tomada o seleccionada de galeria
    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taxi)
        val objetoIntent:Intent=intent

        id = objetoIntent.getStringExtra("id")
        nombreUsuario = objetoIntent.getStringExtra("nombreU")
        telefono = objetoIntent.getStringExtra("telefonoU")
        direccionImagen = objetoIntent.getStringExtra("imagenU")
        correoUsuario=objetoIntent.getStringExtra("correoUs")
        tipo=objetoIntent.getStringExtra("tipoU")
        btnCuentaV.setOnClickListener{
            crearRegistroAuto()}
    }
    lateinit var nombreUsuario:String
    lateinit var id :String
    lateinit var telefono:String
    lateinit var direccionImagen :String
    lateinit var correoUsuario:String
    lateinit var tipo: String


    private fun crearRegistroAuto(){
        /**
         * Creacion de las variables para el inicio de registro
         */
        val marca= etMarca.text.toString()
        val modelo= etModelo.text.toString()
        val placa= etPlaca.text.toString()
        val Ntaxi = etNumeroTaxi.text.toString()
        // Validar que los valores de las variables nombreUsuario, correo y password no
        // se introduzcan con valores vacios
        if (marca.isEmpty() || modelo.isEmpty() || placa.isEmpty()||Ntaxi.isEmpty()){
            Toast.makeText(this, "No se pueden dejar datos en blanco", Toast.LENGTH_LONG).show()
            return
        }
        /**
         *  Implementacion de Firebase
        Creando una instancia de auteticacion con el correo y la contraseña
        Obteniendo los valores de las variables correo y password
        Creando registro de usuario
         */


        guardarUsuarioAFireBase()

    }



    /**
     * Guardar todos los datos de usuario en la Base de Datos de Firebase
     */
    private fun guardarUsuarioAFireBase(){

        val marca= etMarca.text.toString()
        val modelo= etModelo.text.toString()
        val placa= etPlaca.text.toString()
        val Ntaxi = etNumeroTaxi.text.toString()
        val ratingBar=3.5

        //val idUsuario = FirebaseAuth.getInstance().uid ?: ""

        val referenciaBaseDatos = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$id")
        val taxi = Taxi(id, nombreUsuario,correoUsuario,telefono,tipo.toInt() ,direccionImagen,marca,modelo,placa,Ntaxi,ratingBar.toFloat())

        referenciaBaseDatos.setValue(taxi)
            .addOnSuccessListener {
               // Toast.makeText(this, "Se ha guardado con exito el usuario en Storage", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MenuTaxiActivity::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "falló  ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    class Taxi(val idUsuario:String, val nombreUsuario: String, val correo:String,val telefono:String,val tipo:Int,val imagenPerfil: String,val marca:String,val modelo:String,val numeroPlaca:String,val numeroTaxi:String,val rating:Float)

}
