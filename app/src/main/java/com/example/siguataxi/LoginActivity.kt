package com.example.siguataxi

//import com.google.android.gms.auth.api.Auth
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.ConnectionResult
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.firebase.auth.FirebaseAuth
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
    private val usuarioEmail:String = "eduardocalix11xtra@gmail.com"
    private val contraseña:String="abc"
    //var mAuth: FirebaseAuth? = null
    //private val RC_SIGN_IN = 2
    //private var mGoogleApiClient: GoogleApiClient? = null
   // var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var usuarioIngresado: EditText = findViewById(R.id.etUsuario)
        usuarioIngresado.text.toString()
        var contraseñauno: EditText = findViewById(R.id.etContraseña)
        contraseñauno.text.toString()
        //val email = editTextEmail.getText().toString().trim()
        //val password = editTextPassword.getText().toString().trim()


        /*//checking if email and passwords are empty
        if (TextUtils.isEmpty(usuarioI.text)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
            return
        }
*/
        /*button = findViewById(R.id.google_btn);
        google_btn.setOnClickListener(View.OnClickListener (){
             fun onClick(view: View) {
            signIn()
        }})*/

        btnIniciar.setOnClickListener {
            if (usuarioIngresado.text.toString() == usuarioEmail && contraseñauno.text.toString() == contraseña) {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.contraseña_incorrecta, Toast.LENGTH_SHORT).show()
            }
        }

        tvRegistro1.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)

        }

    }
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("Está seguro que desea salir?")
            .setCancelable(false)
            .setPositiveButton(
                "Si"
            ) { _, _ -> this@LoginActivity.finish() }
            .setNegativeButton("No", null)
            .show()
    }


}