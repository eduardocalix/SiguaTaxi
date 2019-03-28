package com.example.siguataxi

import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_registro.*


class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombre:EditText
    private lateinit var etApellidos:EditText
    private lateinit var etCorreo:EditText
    private lateinit var etContrasena:EditText
    private lateinit var etTelefono:EditText
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressBar
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etTelefono = findViewById(R.id.etTelefono)
        etCorreo= findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        progressBar = findViewById(R.id.progressBar2)
        database = FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        dbReference = database.reference.child("User")

        //Evalua que radio boton esta seleccionado para la cracion
        // de los usuarios ya sea taxista o usuario normal



        rbTaxi.setOnClickListener{
        if(rbTaxi.isChecked ){
            rbUsuario.isChecked=false
            btncuenta.setText (R.string.siguiente)
            btncuenta.setOnClickListener {
                    val intent = Intent(this, TaxiActivity::class.java)
                    startActivity(intent)
                   // Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()


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
                crearNuevaCuenta()
          }
            }
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
   fun registro(view: View){
       crearNuevaCuenta()
    }
    private  fun crearNuevaCuenta(){
        val nombre:String=etNombre.text.toString()
        val apellido:String= etApellidos.text.toString()
        val correo:String= etCorreo.text.toString()
        val contrasena : String=etContrasena.text.toString()
        val confirmar : String=etConfirmar.text.toString()
        val telefono:String=etTelefono.text.toString()

        if(!TextUtils.isEmpty(nombre)&&!TextUtils.isEmpty(apellido)&&!TextUtils.isEmpty(contrasena)&&!TextUtils.isEmpty(correo)&&!TextUtils.isEmpty(telefono)&&contrasena==confirmar) {
            progressBar.visibility= View.VISIBLE

            auth.createUserWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener (this){
                    task ->

                    if (task.isComplete){
                        val user: FirebaseUser?=auth.currentUser
                        verifyEmail(user)
                        Toast.makeText(this, "esperando...", Toast.LENGTH_LONG).show()

                        val userBD=dbReference.child(user?.uid.toString())
                        userBD.child("nombres").setValue(nombre).toString()
                        userBD.child("apellidos").setValue(apellido).toString()
                        userBD.child("telefono").setValue(telefono).toString()

                        action()
                    }
                }

        }
    }
    private fun action(){
        startActivity(Intent(this,LoginActivity::class.java))
    }
    private fun verifyEmail(user:FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isComplete) {
                    Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al enviar Email", Toast.LENGTH_LONG).show()

                }
            }
    }
}
