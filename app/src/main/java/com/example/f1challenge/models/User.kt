package com.example.f1challenge.models

data class User(
    var uid : String = "",
    var nombre : String = "",
    var email : String = "",
    val contasenia : String = "",
    val puntos : Int,
    val fotoAvatar : String = ""
)
