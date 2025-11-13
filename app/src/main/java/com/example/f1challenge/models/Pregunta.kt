package com.example.f1challenge.models

data class Pregunta(
    var pid : String = "",
    var texto : String = "",
    val opciones: String, // JSON que guardamos como String (se podria usar List<String>)
    val respuestaCorrecta: Int
)
