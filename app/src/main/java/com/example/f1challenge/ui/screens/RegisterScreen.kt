package com.example.f1challenge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1challenge.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val currentUser = authViewModel.currentUser.collectAsState().value
    val errorMessage by authViewModel.errorMessage.collectAsState()

    //Navega a la pantalla de inicio si el login es exitoso
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onRegisterSuccess()
        }
    }

    //Muestra un Snackbar si hay un error de registro
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            authViewModel.clearError() //Limpia el error después de mostrarlo
        }
    }

    //Envolver en un Scaffold para poder usar Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Registro de Usuario", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (nombre.isBlank() || telefono.isBlank() || email.isBlank() || password.isBlank()) {
                        //Manejar error de campos vacíos
                        return@Button
                    }
                    if (password.length < 6){
                        return@Button
                    }
                    authViewModel.register(email, password, nombre, telefono)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}