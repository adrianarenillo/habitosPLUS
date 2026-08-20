package com.adrian.habitosplus.ui.screens.listahabitos

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adrian.habitosplus.R
import com.adrian.habitosplus.domain.model.Habito
import com.adrian.habitosplus.util.crearArchivoFoto
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.TaskAlt

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
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var habitoIdPendiente by remember { mutableStateOf<Int?>(null) }
    var fotoUriPendiente by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        val idHabito = habitoIdPendiente
        if (idHabito != null) {
            val uriFinal = if (exito) fotoUriPendiente?.toString() else null
            viewModel.marcarCumplidoHoy(idHabito, uriFinal)
            scope.launch {
                snackbarHostState.showSnackbar(
                    if (exito) "¡Hábito marcado con foto! 📸" else "Hábito marcado como cumplido"
                )
            }
        }
        habitoIdPendiente = null
        fotoUriPendiente = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        val idHabito = habitoIdPendiente
        if (concedido && idHabito != null) {
            val nuevaUri = crearArchivoFoto(context)
            fotoUriPendiente = nuevaUri
            cameraLauncher.launch(nuevaUri)
        } else if (idHabito != null) {
            viewModel.marcarCumplidoHoy(idHabito, null)
            habitoIdPendiente = null
            scope.launch {
                snackbarHostState.showSnackbar("Hábito marcado (sin foto, permiso denegado)")
            }
        }
    }

    fun marcarConFoto(idHabito: Int) {
        habitoIdPendiente = idHabito
        val tienePermiso = context.checkSelfPermission(Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            val nuevaUri = crearArchivoFoto(context)
            fotoUriPendiente = nuevaUri
            cameraLauncher.launch(nuevaUri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Hábitos") },
                actions = {
                    IconButton(onClick = onAjustesClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
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
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.fondo_lista_habitos),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
            ) {

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
                                onMarcarCumplido = { marcarConFoto(habito.id) }
                            )
                        }
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
                Icon(Icons.Default.TaskAlt, contentDescription = "Marcar cumplido")
            }
        }
    }
}