package com.adrian.habitosplus.ui.screens.detallehabito

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.adrian.habitosplus.domain.model.RegistroCumplimiento
import com.adrian.habitosplus.util.copiarImagenDeGaleria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleHabitoScreen(
    habitoId: Int,
    viewModel: DetalleHabitoViewModel = viewModel(),
    onBackClick: () -> Unit,
    onEditarClick: () -> Unit
) {
    val habito by viewModel.habito.collectAsState()
    val registros by viewModel.registros.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mostrarDialogoEliminarHabito by remember { mutableStateOf(false) }
    var registroParaCambiarFoto by remember { mutableStateOf<RegistroCumplimiento?>(null) }
    var fotoAmpliada by remember { mutableStateOf<String?>(null) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uriSeleccionada: Uri? ->
        val registro = registroParaCambiarFoto
        registroParaCambiarFoto = null
        if (uriSeleccionada != null && registro != null) {
            scope.launch {
                val uriLocal = withContext(Dispatchers.IO) {
                    copiarImagenDeGaleria(context, uriSeleccionada)
                }
                if (uriLocal != null) {
                    viewModel.actualizarFotoRegistro(registro, uriLocal.toString())
                }
            }
        }
    }

    LaunchedEffect(habitoId) {
        viewModel.cargarHabito(habitoId)
    }

    if (fotoAmpliada != null) {
        FotoAmpliadaDialog(
            fotoUri = fotoAmpliada!!,
            onDismiss = { fotoAmpliada = null }
        )
    }

    if (mostrarDialogoEliminarHabito) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminarHabito = false },
            title = { Text("¿Eliminar este hábito?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoEliminarHabito = false
                    viewModel.eliminarHabito(onBackClick)
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminarHabito = false }) {
                    Text("Cancelar")
                }
            }
        )
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
                    IconButton(onClick = onEditarClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar hábito")
                    }
                    IconButton(onClick = { mostrarDialogoEliminarHabito = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar hábito")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            val fondoUri = habito?.imagenFondoUri

            if (fondoUri != null) {
                AsyncImage(
                    model = fondoUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (fondoUri != null) {
                            Modifier.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
                        } else {
                            Modifier
                        }
                    )
                    .padding(16.dp)
            ) {
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
                        items(registros, key = { it.id }) { registro ->
                            RegistroItem(
                                registro = registro,
                                formato = formato,
                                onEliminar = { viewModel.eliminarRegistro(registro) },
                                onCambiarFoto = {
                                    registroParaCambiarFoto = registro
                                    galeriaLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                onImagenClick = { fotoAmpliada = registro.fotoUri }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistroItem(
    registro: RegistroCumplimiento,
    formato: SimpleDateFormat,
    onEliminar: () -> Unit,
    onCambiarFoto: () -> Unit,
    onImagenClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (registro.fotoUri != null) {
            AsyncImage(
                model = registro.fotoUri,
                contentDescription = "Foto del cumplimiento",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onImagenClick)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Text(
            "• ${formato.format(Date(registro.fecha))}",
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onCambiarFoto) {
            Icon(
                Icons.Default.AddAPhoto,
                contentDescription = if (registro.fotoUri != null) "Cambiar foto" else "Agregar foto desde galería",
                modifier = Modifier.size(20.dp)
            )
        }

        IconButton(onClick = onEliminar) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar registro",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun FotoAmpliadaDialog(fotoUri: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = fotoUri,
                contentDescription = "Foto ampliada",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
            }
        }
    }
}