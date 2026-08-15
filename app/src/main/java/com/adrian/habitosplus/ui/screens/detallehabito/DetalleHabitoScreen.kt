package com.adrian.habitosplus.ui.screens.detallehabito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleHabitoScreen(
    habitoId: Int,
    viewModel: DetalleHabitoViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val habito by viewModel.habito.collectAsState()
    val registros by viewModel.registros.collectAsState()

    LaunchedEffect(habitoId) {
        viewModel.cargarHabito(habitoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habito?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.eliminarHabito(onBackClick) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar hábito")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            habito?.descripcion?.let {
                Text(it, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Historial de cumplimientos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (registros.isEmpty()) {
                Text("Aún no has marcado este hábito como cumplido.")
            } else {
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                LazyColumn {
                    items(registros) { registro ->
                        Text("• ${formato.format(Date(registro.fecha))}")
                    }
                }
            }
        }
    }
}