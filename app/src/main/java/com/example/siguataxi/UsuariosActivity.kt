package com.example.siguataxi

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri

import android.provider.MediaStore
import android.util.Log

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
                    val intent = Intent(this, TaxiActivity::class.java)
                    startActivity(intent)
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
                    crearRegistro()

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
    private fun crearRegistro(){
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
        /**
         *  Implementacion de Firebase
        Creando una instancia de auteticacion con el correo y la contraseña
        Obteniendo los valores de las variables correo y password
        Creando registro de usuario
         */
        Toast.makeText(this, "creando: $correo", Toast.LENGTH_SHORT).show()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, password)
            /**
             * Si el proceso de autenticacion no es completado con exito devolver al usuario para que lo complete
             */
            .addOnCompleteListener {
                if (!it.isSuccessful){
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
                    cargarImagenAFireBase()
                }
            }
            /**
             * Manipulando la Excepcion de ".addOnFailureListener", si falla al crear un usuario
             */
            .addOnFailureListener{
                Toast.makeText(this, "Error al crear su cuenta, Porfavor ingrese los datos correctos ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun cargarImagenAFireBase(){

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
                   //Toast.makeText(this, "La localizacionn de la imagen es: $it", Toast.LENGTH_LONG).show()
                    guardarUsuarioAFireBase(it.toString())
                }
            }
            .addOnFailureListener{

            }
    }

    /**
     * Guardar todos los datos de usuario en la Base de Datos de Firebase
     */
    private fun guardarUsuarioAFireBase(imagenPerfil: String){
        val idUsuario = idInsersion.toString()
        //val idUsuario = FirebaseAuth.getInstance().uid ?: ""
       // Toast.makeText(this, "Hoy si voy a insertar $idUsuario", Toast.LENGTH_LONG).show()

        var referenciaBaseDatos = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$idUsuario")
        val usuario = Usuario(idUsuario, etNombre.text.toString(),etTelefono.text.toString(), imagenPerfil)
       // Toast.makeText(this, "Hoy si voy a insertar", Toast.LENGTH_LONG).show()

        referenciaBaseDatos.setValue(usuario)
            .addOnSuccessListener {
                Toast.makeText(this, "Se ha guardado con exito el usuario en Storage", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "falló  ${it.message}", Toast.LENGTH_SHORT).show()


            }

    }

    class Usuario(val idUsuario:String, val nombreUsuario: String, val telefono:String,val imagenPerfil: String)


}
