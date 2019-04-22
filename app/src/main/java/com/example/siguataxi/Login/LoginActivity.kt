package com.example.siguataxi

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import android.support.v7.app.AppCompatActivity


import android.os.Bundle

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast



//import sun.security.jgss.GSSUtil.login


/**
 * A login screen that offers login via email/password.
 */

class LoginActivity : AppCompatActivity() {
//"gladysberenicejimenez@gmail.com"
/*
    private lateinit var progressBar: ProgressBar
    private lateinit var auth:FirebaseAuth


    private val usuarioEmail:String = "eduardocalix11xtra@gmail.com"
    private val contrasena:String="abc"*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Accion al hacer click en el boton Iniciar Sesión
        btnIniciar.setOnClickListener {

            val correo = etUsuario.text.toString()
            val password = etContrasenaLogin.text.toString()
            if( password.isEmpty()&&correo.isEmpty()){
                Toast.makeText(this, "Contraseña o usuario están en blanco", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // Iniciando Login de usuario ya registrado
            FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful){
                        Toast.makeText(this, "Contraseña o usuario incorrecto", Toast.LENGTH_LONG).show()

                    return@addOnCompleteListener}
                    val intent = Intent(this, MenuActivity::class.java)
                   intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()



                }}
        /*
               // Volver a pantalla de registro
               lblVolverRegistro_login.setOnClickListener {
                   finish()
               }


               var usuarioingresado: EditText = findViewById(R.id.etUsuario)
               usuarioingresado.text.toString()

               var contrasenauno:EditText = findViewById( R.id.etContrasena)
               contrasenauno.text.toString()
               progressBar=findViewById(R.id.progressBar3)
               auth=FirebaseAuth.getInstance()
               val email = editTextEmail.getText().toString().trim()
               val password = editTextPassword.getText().toString().trim()


               //verifica si el correo y la contraseña estan vacias
               if (TextUtils.isEmpty(usuarioI.text)) {
                   Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
                   return
               }
       */
        /*button = findViewById(R.id.google_btn);
        google_btn.setOnClickListener(View.OnClickListener (){
             fun onClick(view: View) {
            signIn()
        }})

        btnIniciar.setOnClickListener {
            if (usuarioingresado.text.toString() == usuarioEmail && contrasenauno.text.toString()== contrasena) {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.contraseña_incorrecta, Toast.LENGTH_SHORT).show()
            }

        }*/

        tvRegistroLogin.setOnClickListener {
            val intent = Intent(this, UsuariosActivity::class.java)
            startActivity(intent)


        }

    }

/*
    private fun UsuarioInicio(){
        val usuario:String=etUsuario.text.toString()
        val contrasena:String=etContrasena.text.toString()

        if (!TextUtils.isEmpty(usuario)&& !TextUtils.isEmpty(contrasena)){
            progressBar.visibility=View.VISIBLE

            auth.signInWithEmailAndPassword(usuario,contrasena)
                .addOnCompleteListener(this){
                    task ->
                    if (task.isSuccessful){
                    action()
                    }else{
                        Toast.makeText(this, "Error en la autenticacion", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }*/

//    private fun action(){
//        val intent = Intent(this, MenuActivity::class.java)
//        startActivity(intent)
//        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
//    }
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("Está seguro que desea salir?")
            .setCancelable(false)
            .setPositiveButton(
                "Si"
            ) { _, _ -> this@LoginActivity.finishAffinity()}
            .setNegativeButton("No", null)
            .show()
    }


}