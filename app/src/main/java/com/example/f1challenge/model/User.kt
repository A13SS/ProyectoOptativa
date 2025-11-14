package com.example.f1challenge.model

data class User(
    var uid : String = "",
    var nombre : String = "",
    var email : String = "",
    var telefono : String = "",
    val contasenia : String = "",
    val puntos : Int = 0
)
