package com.example.siguataxi.Forma

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/**
 * Es una interfaz cuyo propósito es permitir una forma rápida de pasar objetos entre los diversos componentes que Android tiene.
 * Este proceso es más ligero y rápido que el uso de Serializable.
*/
@Parcelize
class Usuario (val idUsuario:String, val nombreUsuario: String, val correo:String,val telefono:String,val tipo:Int,val imagenPerfil: String,val marca:String,val modelo:String,val numeroPlaca:String,val numeroTaxi:String,val rating:Int):Parcelable{
    constructor(): this("","", "","",3,"","","","","",3)
}
