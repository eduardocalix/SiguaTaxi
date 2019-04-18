package com.example.siguataxi.Forma

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/**
 * Es una interfaz cuyo propósito es permitir una forma rápida de pasar objetos entre los diversos componentes que Android tiene.
 * Este proceso es más ligero y rápido que el uso de Serializable.
*/
@Parcelize
class Usuario (val idUsuario:String, val nombreUsuario: String, val telefono:String,val imagenPerfil: String):Parcelable{
    constructor(): this("","", "","")
}
