package com.adrian.habitosplus.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun crearArchivoFoto(context: Context): Uri {
    val carpetaFotos = File(context.cacheDir, "fotos")
    if (!carpetaFotos.exists()) {
        carpetaFotos.mkdirs()
    }
    val archivo = File(carpetaFotos, "foto_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}