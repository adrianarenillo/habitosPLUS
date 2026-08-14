package com.adrian.habitosplus.domain.model

data class RegistroCumplimiento(
    val id: Int = 0,
    val idHabito: Int,
    val fecha: Long,
    val fotoUri: String?
)