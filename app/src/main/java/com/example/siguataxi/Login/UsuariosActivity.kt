package com.example.siguataxi

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri

import android.provider.MediaStore

import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage



import kotlinx.android.synthetic.main.activity_usuarios.*

import java.util.*

class UsuariosActivity : AppCompatActivity() {
    companion object {
        val TAG = "UsuariosActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        rbTaxi.setOnClickListener{
            if(rbTaxi.isChecked ){
                rbUsuario.isChecked=false
                btncuenta.setText (R.string.siguiente)
                btncuenta.setOnClickListener {
                    crearRegistro(1)

                    // Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()
                }
            }else{
                rbTaxi.isChecked=false
                btncuenta.setText(R.string.crear_cuenta)
            }}
        /**
         * Accion al hacer click en el boton registro
         */

        rbUsuario.setOnClickListener{
            if(rbUsuario.isChecked){
                rbTaxi.isChecked=false
                btncuenta.setText(R.string.crear_cuenta)

                btncuenta.setOnClickListener {
                    crearRegistro(0)

                }
            }
        }

        /**
         * Mostrando pantalla de Login
         */
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Boton agregar foto
        seleccionarfotoboton.setOnClickListener {
            /**
             * Accediendo a la galeria de fotos para seleccionar una imagen de perfil
             */
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
            //Toast.makeText(this,"Intentando mostrar foto para seleccionarla",Toast.LENGTH_SHORT).show()
        }
    }

    var ubicacionImagenSeleccionada: Uri? = null
    /**
     * Sobre escribiendo funcion de Activity Result para capturar la imagen del usuario
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            /**
             * Proceder a verificar que imagen fue seleccionada
             */
            Toast.makeText(this,"La foto fue seleccionada",Toast.LENGTH_SHORT).show()
            /**
             * Encontrando la ubicacion de la imagen
             */
            ubicacionImagenSeleccionada = data.data

            // Obteniendo la imagen del usuario
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ubicacionImagenSeleccionada)

            /**
             * Agregando imagen seleccionada al boton de "Agregar Foto"
             */
            selectphoto_imageview_register.setImageBitmap(bitmap)
            seleccionarfotoboton.alpha = 0f

            /**
             * Agregar imagen al boton de Agregar Foto
             */

            //val bitmapDrawable = BitmapDrawable(bitmap)
            //btnFoto_registro.setBackgroundDrawable(bitmapDrawable)

        }
    }
lateinit var idInsersion :String
    /**
     * Funcion para el registro de Usuario
     */
    private fun crearRegistro( valor : Int){
        /**
         * Creacion de las variables para el inicio de registro
         */
        val nombreUsuario = etNombre.text.toString()
        val correo = etCorreo.text.toString()
        val password = etContrasena.text.toString()
        val telefono=etTelefono.text.toString()

        // Validar que los valores de las variables nombreUsuario, correo y password no
        // se introduzcan con valores vacios
        if (nombreUsuario.isEmpty() || correo.isEmpty() || password.isEmpty()||telefono.isEmpty()){
            Toast.makeText(this, "No se pueden dejar datos en blanco", Toast.LENGTH_LONG).show()
            return
        }
        if( password.length>6){
            Toast.makeText(this, "Contraseña muy corta, tiene que tener mas de 6 caracteres", Toast.LENGTH_LONG).show()
            return
        }

        /**
         *  Implementacion de Firebase
        Creando una instancia de auteticacion con el correo y la contraseña
        Obteniendo los valores de las variables correo y password
        Creando registro de usuario
         */
        //Toast.makeText(this, "creando: $correo", Toast.LENGTH_SHORT).show()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, password)
            /**
             * Si el proceso de autenticacion no es completado con exito devolver al usuario para que lo complete
             */
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    Toast.makeText(this, "Ocurrió un error", Toast.LENGTH_LONG).show()

                    return@addOnCompleteListener
                }else {

                    /**
                     * si es completado con exito
                     */

                    Toast.makeText(this,"Su cuenta se ha creado con exito :${it.result?.user?.uid}",Toast.LENGTH_SHORT).show()
                    idInsersion=it.result?.user?.uid.toString()
                    /**
                     * Agregar imagen a Firebase
                     */

                    cargarImagenAFireBase(valor)


                }
            }
            /**
             * Manipulando la Excepcion de ".addOnFailureListener", si falla al crear un usuario
             */
            .addOnFailureListener{
                Toast.makeText(this, "Error al crear su cuenta, Porfavor ingrese los datos correctos ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun cargarImagenAFireBase(valoruno: Int){

        if (ubicacionImagenSeleccionada == null ){
            return
        }
        /**
         * Agregado un nombre de archivo con ID Unico para cada usuario
         */
        val nombreArchivo = UUID.randomUUID().toString()

        /**
         * Agregando la imagen del usuario al almacenamiento creado en FireBase
         */
        val referenciaAlmacenamiento = FirebaseStorage.getInstance().getReference("/imagenesUsuario/$nombreArchivo")

        referenciaAlmacenamiento.putFile(ubicacionImagenSeleccionada!!)
            .addOnSuccessListener {
                /**
                 * Descarga de imagen de Storage Firebase
                 */
                referenciaAlmacenamiento.downloadUrl.addOnSuccessListener{
                    it.toString()
                    guardarUsuarioAFireBase(it.toString(),valoruno)

                }
            }
            .addOnFailureListener{

            }
    }

    /**
     * Guardar todos los datos de usuario en la Base de Datos de Firebase
     */
    private fun guardarUsuarioAFireBase(imagenPerfil: String,valoruno: Int){
        val ratingBar=3.5

        val idUsuario = idInsersion.toString()
        //val idUsuario = FirebaseAuth.getInstance().uid ?: ""
        if (valoruno==1){
           //Toast.makeText(this, "Entró a la parte taxista", Toast.LENGTH_SHORT).show()

            guardarTaxista(imagenPerfil,valoruno)
        }else{
        val referenciaBaseDatos = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$idUsuario")
        val usuario = Usuario2(idUsuario, etNombre.text.toString(),etCorreo.text.toString(),etTelefono.text.toString(),valoruno ,imagenPerfil,"","","","",ratingBar.toFloat())

        referenciaBaseDatos.setValue(usuario)
            .addOnSuccessListener {
               // Toast.makeText(this, "Se ha guardado con exito el usuario en Storage", Toast.LENGTH_SHORT).show()

                val intent = Intent(this,MenuActivity::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)}

            .addOnFailureListener {
                Toast.makeText(this, "falló  ${it.message}", Toast.LENGTH_SHORT).show()
            }}
    }

    class Usuario2(val idUsuario:String, val nombreUsuario: String, val correo:String,val telefono:String,val tipo:Int,val imagenPerfil: String,val marca:String,val modelo:String,val numeroPlaca:String,val numeroTaxi:String,val rating:Float)
/**
*Pasa el id despues de guardar el usuario para guardar los datos del vehiculo si es taxista
 */
    private fun guardarTaxista(imagenPerfil: String,valoruno: Int){
        val idUsuarioTaxi = idInsersion
        val nombre =etNombre.text.toString()
        val telefono =etTelefono.text.toString()
        val correo= etCorreo.text.toString()
        val tipo:String=valoruno.toString()

        val intent = Intent(this, TaxiActivity::class.java)
        intent.putExtra("id",idUsuarioTaxi)
        intent.putExtra("nombreU",nombre)
        intent.putExtra("telefonoU",telefono)
        intent.putExtra("imagenU",imagenPerfil)
        intent.putExtra("correoUs",correo)
        intent.putExtra("tipoU",tipo)
        startActivity(intent)
    finish()
    }


}
