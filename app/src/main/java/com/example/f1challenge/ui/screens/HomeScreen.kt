package com.example.f1challenge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1challenge.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    onNavigateToEventList: () -> Unit,
    onLogoutSuccess: () -> Unit,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.currentUser.collectAsState()

    LaunchedEffect(user) {
        if (user == null) {
            onLogoutSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("BIENVENIDO A LA APLICACIÓN F1 CHALLENGE, TE ESPERAMOS...", fontSize = 28.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))

            user?.let { authUser ->
                Text("Email: ${authUser.email}", fontSize = 18.sp)
            }

        Spacer(modifier = Modifier.height(32.dp))

        //Botón para ir a la lista de eventos
        Button(
            onClick = { onNavigateToEventList() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Eventos")
        }


        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { authViewModel.logout() }) {
            Text("Cerrar Sesión")
        }
    }
}