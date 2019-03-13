package com.example.siguataxi

import android.content.DialogInterface
import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_registro.*






class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //Evalua que radio boton esta seleccionado para la cracion
        // de los usuarios ya sea taxista o usuario normal

        var nombre: EditText = findViewById(R.id.etNombre)
        nombre.text.toString()
        var usuarioIngresado: EditText = findViewById(R.id.etCorreo)
        usuarioIngresado.text.toString()
        var contraseñauno: EditText = findViewById(R.id.etContraseña)
        contraseñauno.text.toString()
        var contraseñados: EditText = findViewById(R.id.etConfirmar)
        contraseñados.text.toString()
        var telefono: EditText = findViewById(R.id.etTelefono)
        telefono.text.toString()
        rbTaxi.setOnClickListener{
        if(rbTaxi.isChecked ){
            rbUsuario.isChecked=false
            btncuenta.setText (R.string.siguiente)
            btncuenta.setOnClickListener {
                if(nombre.text.toString() != "" && usuarioIngresado.text.toString()!="" && contraseñauno.text.toString()!="" && contraseñados.text.toString()!="" && telefono.text.toString()!=""){

                    val intent = Intent(this, TaxiActivity::class.java)
                startActivity(intent)}else{
                    Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()
                }

            }

        }else{
            rbTaxi.isChecked=false
            btncuenta.setText(R.string.crear_cuenta)

        }}

        rbUsuario.setOnClickListener{
            if(rbUsuario.isChecked){
                rbTaxi.isChecked=false
                btncuenta.setText(R.string.crear_cuenta)
                btncuenta.setOnClickListener {
                    if(nombre.text.toString() != "" && usuarioIngresado.text.toString()!="" && contraseñauno.text.toString()!="" && contraseñados.text.toString()!="" && telefono.text.toString()!=""){

                        Toast.makeText(this, R.string.usuarioCreado, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()
                }}
            }
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




}
