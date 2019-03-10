package com.example.siguataxi

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*

import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */

class LoginActivity : AppCompatActivity() {

    private val usuario:String = "ecalix@gmail.com"
    private val contraseña:String="abc"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var usuarioI:EditText = findViewById(R.id.etUsuario)
        usuarioI.getText().toString()
        var contraseña1:EditText=findViewById(R.id.etContraseña)
        contraseña1.getText().toString()



    btnIniciar.setOnClickListener {
        if(usuarioI.text.toString() == usuario && contraseña1.text.toString()==contraseña){
    val intent:Intent = Intent(this, MapaTaxiActivity::class.java)
    startActivity(intent)
    finish()}else{
            Toast.makeText(this, R.string.contraseña_incorrecta, Toast.LENGTH_SHORT).show()
        }}

        tvRegistro.setOnClickListener {
            val intent:Intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
            finish()}
    }
}