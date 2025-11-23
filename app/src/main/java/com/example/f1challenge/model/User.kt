package com.example.f1challenge.model
data class User(
    var nombre : String = "",
    var email : String = "",
    var telefono : String = "",
    val contasenia : String = "",
    val puntos : Int = 0,
    val rol : Int = 2  //Admin 1, usuario 2
) {
    //Propiedad para verificar si el usuario es un admin o no
    val isAdministrator: Boolean
        get() = rol == 1
}
