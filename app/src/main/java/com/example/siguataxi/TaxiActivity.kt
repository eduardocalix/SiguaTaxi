package com.example.siguataxi

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_taxi.*

class TaxiActivity : AppCompatActivity() {

    //La variable fileUri Almacena la foto tomada o seleccionada de galeria
    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taxi)

        //al darle click a galeria llama la funcion predeterminada
        galeria.setOnClickListener {
            pickPhotoFromGallery()
        }

        //al darle click a tomar foto
        tomaFoto.setOnClickListener {
            askCameraPermission()
        }

        var marca: EditText = findViewById(R.id.etMarca)
        marca.text.toString()
        var modelo: EditText = findViewById(R.id.etModelo)
        modelo.text.toString()
        var placa: EditText = findViewById(R.id.etPlaca)
        placa.text.toString()
        var Ntaxi: EditText = findViewById(R.id.etNumeroTaxi)
        Ntaxi.text.toString()

        btnCuentaV.setOnClickListener{
            /*val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)*/
            if(marca.text.toString()!="" && modelo.text.toString()!="" && placa.text.toString()!="" && Ntaxi.text.toString()!="" ){

                Toast.makeText(this, R.string.usuarioCreado, Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()
        }}
    }

//Funcion para poder acceder a la galeria
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickImageIntent, AppConstants.PICK_PHOTO_REQUEST)
    }

    //lanza la cámara para tomar una foto a través de la intención
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, AppConstants.TAKE_PHOTO_REQUEST)
        }
    }

    //Este codigo pregunta si se le da permiso a la camara
   private fun askCameraPermission(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if(report.areAllPermissionsGranted()){
                        //Una vez concedidos los permisos, inicia la cámara.
                        launchCamera()
                    }else{
                        Toast.makeText(this@TaxiActivity, "Todos los permisos necesitan ser aceptados para usar la camara", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    //show alert dialog with permission options
                    AlertDialog.Builder(this@TaxiActivity)
                        .setTitle(
                            "Error de permisos!")
                        .setMessage(
                            "Por favor aceptar los permisos para usar la camara")
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog, _ -> dialog.dismiss()
                            token.cancelPermissionRequest()
                        }
                        .setPositiveButton(android.R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token.continuePermissionRequest()
                        }
                        .setOnDismissListener {
                            token.cancelPermissionRequest() }
                        .show()
                }

            }).check()

    }

    // anular la función que se llama una vez que se ha tomado la foto
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.TAKE_PHOTO_REQUEST) {
            //Foto de la camara
            //mostrar la foto en el imageview
            imFoto.setImageURI(fileUri)
        }else if(resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.PICK_PHOTO_REQUEST){
            //Foto de la galeria
            fileUri = data?.data
            imFoto.setImageURI(fileUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


}

    //Funcion que nos permite salir del activity si
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("Está seguro que desea salir?")
            .setCancelable(false)
            .setPositiveButton(
                "Si"
            ) { _, _ -> this@TaxiActivity.finish() }
            .setNegativeButton("No", null)
            .show()
    }

    /*// Agregar un nuevo platillo (repetido)
    platillos.add(Platillo("Platillo 11", 300.00, 4.0, R.drawable.platillo01))
    adapter.notifyDataSetChanged()*/
}
