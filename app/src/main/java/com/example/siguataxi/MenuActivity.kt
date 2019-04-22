package com.example.siguataxi


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.siguataxi.Forma.Usuario
import com.example.siguataxi.Mensajes.NuevoMensajeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import kotlinx.android.synthetic.main.nav_header_menu.*


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        verificarUsuario()
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val ft = mFragmentManager.beginTransaction()
            ft.replace(R.id.MapaPrincipal,ContenedorMensajesActivity()).commit()
        }
 /*       configuracion.setOnClickListener{
        }*/
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mFragmentManager = supportFragmentManager
        mFragmentTransaction = mFragmentManager.beginTransaction()

    }
    private fun verificarUsuario(){
        val idUsuario = FirebaseAuth.getInstance().uid
        if (idUsuario == null){
            val intent = Intent (this, UsuariosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{buscarUsuario()}
    }
    private val tipo=0
    lateinit var nombreCuenta:String
    private fun buscarUsuario() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {


                //MenuTaxiActivity.usuarioActual = p0.getValue(Usuario::class.java)
                val nombreUsuario = p0.getValue(Usuario::class.java)
                if (nombreUsuario != null) {

                  //  Log.d("UltimosMensajes", "Usuario Actual ${MenuTaxiActivity.usuarioActual?.imagenPerfil}")
                    tvNombreUsuario.text = nombreUsuario.nombreUsuario
                    nombreCuenta=nombreUsuario.nombreUsuario
                    tvCorreoMenu.text=nombreUsuario.correo
                    tipo== nombreUsuario.tipo
                    Picasso.get().load(nombreUsuario.imagenPerfil).into(imageView)

                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.configuracion ->{
                Snackbar.make(mNavigationView,"No se puede configurar en este momento!!" +
                        "" +
                        "", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            return true}

            else -> return super.onOptionsItemSelected(item)
        }
    }

    lateinit var mDrawerLayout: DrawerLayout
    lateinit var mNavigationView: NavigationView
    lateinit var mFragmentManager: FragmentManager
    lateinit var mFragmentTransaction:FragmentTransaction
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_inicio -> {

          val ft = mFragmentManager.beginTransaction()
               ft.replace(R.id.MapaPrincipal, BlankFragment()).commit()
            }
            R.id.nav_pedir -> {
               /* val intent = Intent(this, NuevoMensajeActivity::class.java)
                startActivity(intent)*/
                val ft = mFragmentManager.beginTransaction()
                ft.replace(R.id.MapaPrincipal,NuevoMensajeActivity()).commit()

            }
            R.id.nav_mostrar -> {
                val ft = mFragmentManager.beginTransaction()
                ft.replace(R.id.MapaPrincipal,ContenedorMensajesActivity()).commit()
                /*val intent = Intent(this, ContenedorMensajesActivity::class.java)
                startActivity(intent)*/
            }
            R.id.nav_cerrar -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_horarios -> {
                val ft = mFragmentManager.beginTransaction()
                ft.replace(R.id.MapaPrincipal,HorariosActivity()).commit()
            }
            R.id.nav_nosotros -> {
            val ft = mFragmentManager.beginTransaction()
                ft.replace(R.id.MapaPrincipal,NosotrosActivity()).commit()

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()
    }



    }


