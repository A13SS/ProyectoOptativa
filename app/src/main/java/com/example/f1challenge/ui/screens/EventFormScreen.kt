package com.example.f1challenge.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.f1challenge.model.Event
import com.example.f1challenge.viewmodel.AuthViewModel
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(
    eventId: String,
    onBack: () -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val userData by authViewModel.userData.collectAsState()

    // Solo admins
    if (userData?.isAdministrator != true) {
        LaunchedEffect(Unit) { onBack() }
        return
    }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    //Cargar si es edición
    LaunchedEffect(eventId) {
        if (eventId.isNotEmpty()) {
            FirebaseDatabase.getInstance("https://f1challenge-4fb78-default-rtdb.europe-west1.firebasedatabase.app/").getReference("elementos").child(eventId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val event = snapshot.getValue(Event::class.java)
                    event?.let {
                        nombre = it.nombre
                        descripcion = it.descripcion
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (eventId.isEmpty()) "Nuevo Evento" else "Editar Evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del evento") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Button(
                onClick = {
                    if (nombre.isBlank() || descripcion.isBlank()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val event = Event(
                        nombre = nombre,
                        descripcion = descripcion,
                        fechaCreacion = fecha
                    )

                    val ref = FirebaseDatabase.getInstance("https://f1challenge-4fb78-default-rtdb.europe-west1.firebasedatabase.app/").getReference("elementos")

                    if (eventId.isEmpty()) {
                        //Crear
                        ref.push().setValue(event)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Evento creado", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        //Editar
                        ref.child(eventId).setValue(event)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Evento actualizado", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}