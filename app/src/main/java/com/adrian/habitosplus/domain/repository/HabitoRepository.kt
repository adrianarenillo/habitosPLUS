package com.adrian.habitosplus.domain.repository

import com.adrian.habitosplus.domain.model.Habito
import com.adrian.habitosplus.domain.model.RegistroCumplimiento
import kotlinx.coroutines.flow.Flow

interface HabitoRepository {

    fun getAllHabitos(): Flow<List<Habito>>

    suspend fun getHabitoById(id: Int): Habito?

    suspend fun insertHabito(habito: Habito): Long

    suspend fun updateHabito(habito: Habito)

    suspend fun deleteHabito(habito: Habito)

    fun getRegistrosByHabito(idHabito: Int): Flow<List<RegistroCumplimiento>>

    suspend fun marcarCumplidoHoy(idHabito: Int, fotoUri: String?)

    suspend fun updateRegistro(registro: RegistroCumplimiento)

    suspend fun deleteRegistro(registro: RegistroCumplimiento)
}