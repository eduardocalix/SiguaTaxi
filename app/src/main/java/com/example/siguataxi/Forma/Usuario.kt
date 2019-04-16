package com.example.siguataxi

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Usuario (val idUsuario: String, val nombreUsuario: String, val imagenPerfil: String): Parcelable {
    constructor(): this("","", "")
}
