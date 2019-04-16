package com.example.siguataxi

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_datos.*

class datosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)


        // Crear el ArrayList que contiene la información
        // Data Source
        val conductores = ArrayList<Conductores>()

        conductores.add(Conductores("Dopinder ", "+(504)95767893","2435","PBJ6575",5.0,R.drawable.taxi1))
        conductores.add(Conductores("Baby", "+(504)95734336","2425","PHY6435",4.0,R.drawable.taxi2))
        conductores.add(Conductores("Frank Martin", "+(504)94667898","1565","PKE5975",4.0,R.drawable.taxi3))
        conductores.add(Conductores("Ricky Bobby", "+(504)93576733","1721","PLK5265",4.0,R.drawable.taxi4))
       /* conductores.add(Conductores("Conductores 5", 250.00, 4.0, R.drawable.platillo05))
        conductores.add(Conductores("Conductores 6", 250.00, 4.0, R.drawable.platillo06))
        conductores.add(Conductores("Conductores 7", 250.00, 4.0, R.drawable.platillo07))
        conductores.add(Conductores("Conductores 8", 250.00, 4.0, R.drawable.platillo08))
        conductores.add(Conductores("Conductores 9", 250.00, 4.0, R.drawable.platillo09))
        conductores.add(Conductores("Conductores 10", 250.00, 4.0, R.drawable.Conductores10))*/

        // Definir la variable que se encargar de manejar el widget del RecyclerView
        val laLista = findViewById<RecyclerView>(R.id.rvLista)
        laLista.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        laLista.layoutManager = layoutManager

        // Adaptador
        val adapter = Adaptador(this,conductores, object: ClickLista {
            override fun onClick(view: View, index: Int) {
                Toast.makeText(applicationContext, conductores[index].nombre, Toast.LENGTH_SHORT).show()
            }
        }, object: LongClickListener {
            override fun longClickListener(view: View, index: Int) {
                Log.d("LONG", "Long click listener")
            }
        })

        laLista.adapter = adapter

        // Swipe to refresh
        // Actualizaría los valores de la lista
        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        swipeToRefresh.setOnRefreshListener {
            Log.d("REFRESH", "La información se ha refrescado")
            for (i in 1..10000000) {

            }
            swipeToRefresh.isRefreshing = false


        }
    }
    override fun onBackPressed() {

           this@datosActivity.finish()


    }

}
