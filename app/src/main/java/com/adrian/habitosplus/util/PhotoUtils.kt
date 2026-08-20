package com.adrian.habitosplus.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

private fun carpetaFotos(context: Context): File {
    val carpeta = File(context.cacheDir, "fotos")
    if (!carpeta.exists()) {
        carpeta.mkdirs()
    }
    return carpeta
}

fun crearArchivoFoto(context: Context): Uri {
    val archivo = File(carpetaFotos(context), "foto_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}

fun copiarImagenDeGaleria(context: Context, uriOrigen: Uri): Uri? {
    val archivo = File(carpetaFotos(context), "foto_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uriOrigen)?.use { input ->
        archivo.outputStream().use { output ->
            input.copyTo(output)
        }
    } ?: return null

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}