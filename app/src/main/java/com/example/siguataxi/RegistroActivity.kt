package com.example.siguataxi
import android.content.DialogInterface
import android.widget.LinearLayout
import android.widget.RadioButton
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_taxi.*





class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        rbTaxi.setOnClickListener{
        if(rbTaxi.isChecked == true){
            rbUsuario.isChecked=false
            btncuenta.setText (R.string.siguiente)
            btncuenta.setOnClickListener {
                val intent: Intent = Intent(this, TaxiActivity::class.java)
                startActivity(intent)

            }

        }else{
            rbTaxi.isChecked=false
            btncuenta.setText(R.string.crear_cuenta)

        }}

        rbUsuario.setOnClickListener{
            if(rbUsuario.isChecked==true){
                rbTaxi.isChecked=false
                btncuenta.setText(R.string.crear_cuenta)
            }
        }

        tvLogin.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("Está seguro que desea salir?")
            .setCancelable(false)
            .setPositiveButton(
                "Si"
            ) { dialog, id -> this@RegistroActivity.finishAffinity() }
            .setNegativeButton("No", null)
            .show()
    }
}
