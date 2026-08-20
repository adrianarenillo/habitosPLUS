package com.adrian.habitosplus.domain.model

data class Habito(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String?,
    val colorTag: Int,
    val fechaCreacion: Long,
    val rachaActual: Int = 0,
    val imagenFondoUri: String? = null
)