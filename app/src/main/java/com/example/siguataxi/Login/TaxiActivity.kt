package com.example.siguataxi

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import android.widget.Toast

import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_taxi.*


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


        btnCuentaV.setOnClickListener{
            crearRegistroAuto()}
    }
    lateinit var nombreUsuario:String
    lateinit var id :String
    lateinit var telefono:String
    lateinit var direccionImagen :String

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


        guardarUsuarioAFireBase(direccionImagen)

    }



    /**
     * Guardar todos los datos de usuario en la Base de Datos de Firebase
     */
    private fun guardarUsuarioAFireBase(imagenPerfil: String){

        val marca= etMarca.text.toString()
        val modelo= etModelo.text.toString()
        val placa= etPlaca.text.toString()
        val Ntaxi = etNumeroTaxi.text.toString()

        //val idUsuario = FirebaseAuth.getInstance().uid ?: ""

        val referenciaBaseDatos = FirebaseDatabase.getInstance().getReference("/infoTaxi/$id")
        val taxi = Taxi(id, nombreUsuario,telefono, imagenPerfil,marca,modelo,placa,Ntaxi)

        referenciaBaseDatos.setValue(taxi)
            .addOnSuccessListener {
                Toast.makeText(this, "Se ha guardado con exito el usuario en Storage", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "falló  ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    class Taxi(val idUsuario:String, val nombreUsuario: String, val telefono:String,val imagenPerfil: String,val marca:String,val modelo:String,val numeroPlaca:String,val numeroTaxi:String)


    //Funcion que nos permite salir del activity si
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("Está seguro que desea salir?")
            .setCancelable(false)
            .setPositiveButton(
                "Si"
            ) { _, _ -> this@TaxiActivity.finish() }
            .setNegativeButton("No", null)
            .show()
    }

    /*// Agregar un nuevo platillo (repetido)
    platillos.add(Platillo("Platillo 11", 300.00, 4.0, R.drawable.platillo01))
    adapter.notifyDataSetChanged()*/
}
