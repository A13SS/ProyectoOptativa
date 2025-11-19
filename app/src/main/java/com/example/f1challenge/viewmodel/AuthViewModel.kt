package com.example.f1challenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1challenge.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database("https://f1challenge-4fb78-default-rtdb.europe-west1.firebasedatabase.app/").reference

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    init {
        //Escucha los cambios en el estado de autenticación
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
            if (firebaseAuth.currentUser != null) {
                fetchUserData(firebaseAuth.currentUser!!.uid)
            } else {
                _userData.value = null
            }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            _errorMessage.value = null
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }

    fun register(email: String, password: String, nombre: String, telefono: String) = viewModelScope.launch {
        try {
            _errorMessage.value = null
            if (email.isBlank() || password.isBlank() || nombre.isBlank() || telefono.isBlank()) {
                _errorMessage.value = "Completa todos los campos"
                return@launch
            }

            if (password.length < 6) {
                _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
                return@launch
            }
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return@launch

            //Crea el objeto de usuario para la BD (sin contraseña)
            val user = User(
                nombre = nombre,
                email = email,
                telefono = telefono,
                contasenia = password,  //MIRAR PORSI PREGUNTAR A LA PROFE PARA GUARDAR O NO LA CONTRASEÑA DIRECTAMENTE
                puntos = 0,
                rol = 2
            )

            //Guarda en Realtime Database
            database.child("user").child(uid).setValue(user).await()
        } catch (e: Exception) {
            _errorMessage.value = e.message  //Mostrará el error real
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun fetchUserData(uid: String) = viewModelScope.launch {
        try {
            val snapshot = database.child("user").child(uid).get().await()
            val user = snapshot.getValue(User::class.java)
            _userData.value = user
        } catch (e: Exception) {
            _errorMessage.value = "Error al cargar datos del usuario: ${e.message}"
        }
    }

}
