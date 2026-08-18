package com.adrian.habitosplus.ui.screens.listahabitos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.habitosplus.domain.model.Habito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaHabitosScreen(
    viewModel: ListaHabitosViewModel,
    onHabitoClick: (Int) -> Unit,
    onAjustesClick: () -> Unit,
    onAgregarClick: () -> Unit
) {
    val habitos by viewModel.habitos.collectAsState()
    val quoteState by viewModel.quoteState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Hábitos") },
                actions = {
                    IconButton(onClick = onAjustesClick) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Ajustes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hábito")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            FraseDelDiaCard(quoteState)

            if (habitos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no tienes hábitos. ¡Agrega uno!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    items(habitos) { habito ->
                        HabitoItem(
                            habito = habito,
                            onClick = { onHabitoClick(habito.id) },
                            onMarcarCumplido = { viewModel.marcarCumplidoHoy(habito.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FraseDelDiaCard(quoteState: QuoteState) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            when (quoteState) {
                is QuoteState.Cargando -> Text("Cargando frase motivacional...")
                is QuoteState.Exito -> Text("\"${quoteState.quote.texto}\" — ${quoteState.quote.autor}")
                is QuoteState.Error -> Text("No se pudo cargar la frase (sin conexión)")
            }
        }
    }
}

@Composable
fun HabitoItem(
    habito: Habito,
    onClick: () -> Unit,
    onMarcarCumplido: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(habito.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Racha: ${habito.rachaActual} días", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onMarcarCumplido) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Marcar cumplido")
            }
        }
    }
}