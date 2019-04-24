package com.example.siguataxi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import android.util.Log
import com.example.siguataxi.Forma.Credenciales
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var progressBarStatus = 0
    var dummy:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val city: String = "comayagua"
        val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=10bc1fcd8615b888c89f52126f7e90c7&lang=es&units=metric"

// ProgressBar Determinate
// Obtener las referencias del los objetos
       // var btnDeterminate = this.btnDeterminate
        val prgBarDeterminate = this.prgBarDeterminate
// Evento on click del Button
       // btnDeterminate.setOnClickListener { v ->

        if (Conexion.verificarConexion(this)) {
            solicitudHTTPVolley(url)
            //Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show()
        }
            // Ejecuta el Thread
            Thread(Runnable {
                // hilo ficticio que imita alguna operación cuyo progreso se puede rastrear
                while (progressBarStatus < 100) {
//realizando alguna operación falsa
                    try {
                        dummy = dummy+5
                        Thread.sleep(200)
                        if (dummy == 100){
/*
                            val credencial=Credenciales()::class.java
                            val usuarioLogin=credencial.correoCredencial
                            val contrasena=credencial.contrasenaCredecial
                            if(usuarioLogin.isEmpty() &&contrasena.isEmpty()){*/
                            val intent:Intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()/*}else{
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(usuarioLogin,contrasena)
                                    .addOnCompleteListener {
                                        if (!it.isSuccessful){
                                            Toast.makeText(this, "Contraseña o usuario incorrecto", Toast.LENGTH_LONG).show()

                                            return@addOnCompleteListener}

                                        val intent = Intent(this, MenuActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                                        startActivity(intent)
                                        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()



                                    }
                            }*/

                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
// seguimiento del progreso
                    progressBarStatus = dummy
// Actualizando la barra de progreso
                    prgBarDeterminate.progress = progressBarStatus
                }

            }).start()

        // Verificar si existe conexion a Internet

    }

    private fun solicitudHTTPVolley(url: String) {
        // Instanciar la cola de peticiones
        val queue = Volley.newRequestQueue(this)

        // Obtener un string de respuesta desde la URL solicitada
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d("HTTPVolley", response)
                // Toast.makeText(this, "Conexion establecida", Toast.LENGTH_SHORT).show()

            },
            Response.ErrorListener {
                Log.d("HTTPVolley", "Error en la URL $url")
                Toast.makeText(this, "¡Ha ocurrido un error en la conexión!", Toast.LENGTH_SHORT).show()
            }
        )

        // Agregar la peticion a la cola de peticiones
        queue.add(stringRequest)
    }

    // }

        /*
// ProgressBar Indeterminado
// obtener las referencias del archivo de diseño
        val btnIndeterminate = this.btnIndeterminate
        val prgBarIndeterminate: ProgressBar = this.prgBarIndeterminate
// cuando se hace clic en el botón, comienza la tarea
        btnIndeterminate.setOnClickListener { v ->

            // la tarea se ejecuta en un hilo
            Thread(Runnable {
                // hilo ficticio que imita alguna operación cuyo progreso no se puede rastrear
// mostrar la barra de progreso indefinida
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    prgBarIndeterminate.visibility = View.VISIBLE
                })

// realizando alguna operación ficticia de toma de tiempo
                try {
                    var i=0;
                    while(i<Int.MAX_VALUE){
                        i++
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

// cuando la tarea se completa, ha progresoBar terminado
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    prgBarIndeterminate.visibility = View.GONE
                })
            }).start()
        }*/
/*
// ProgressBar Dinamico
// Crear ProgressBar dinamico.
        val progressBar = ProgressBar(this)
        progressBar.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val linearLayout = findViewById<LinearLayout>(R.id.linearDinamico)
// Agregar ProgressBar al LinearLayout
        linearLayout?.addView(progressBar)

        val btn = findViewById<Button>(R.id.btnDinamico)
        btn?.setOnClickListener {
            val visibility = if (progressBar.visibility == View.GONE) View.VISIBLE else View.GONE
            progressBar.visibility = visibility

            val btnText = if (progressBar.visibility == View.GONE) "Mostrar PROGRESSBAR" else "Ocultar PROGRESSBAR"
            btn.text = btnText
        }*/


}