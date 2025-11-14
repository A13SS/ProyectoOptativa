package com.example.f1challenge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.f1challenge.navigation.AppNavigation
import com.example.f1challenge.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

@Composable
fun F1ChallengeApp(
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    //Inicializar Firebase una sola vez
    LaunchedEffect(Unit) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }

    //Crear el NavController
    val navController = rememberNavController()

    //Pasar tanto el navController como el authViewModel
    AppNavigation(
        navController = navController,
        authViewModel = authViewModel
    )
}