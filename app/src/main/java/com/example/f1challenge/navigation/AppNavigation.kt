package com.example.f1challenge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.f1challenge.ui.screens.HomeScreen
import com.example.f1challenge.ui.screens.LoginScreen
import com.example.f1challenge.ui.screens.RegisterScreen
import com.example.f1challenge.viewmodel.AuthViewModel

//Define las rutas de forma segura
sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
}

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    //Observa el estado de autenticación
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                authViewModel = authViewModel
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                authViewModel = authViewModel
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onLogoutSuccess = { navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } } },
                authViewModel = authViewModel
            )
        }
    }
}