package com.adrian.habitosplus.data.repository

import com.adrian.habitosplus.data.local.HabitoDao
import com.adrian.habitosplus.data.local.RegistroCumplimientoDao
import com.adrian.habitosplus.data.local.entities.HabitoEntity
import com.adrian.habitosplus.data.local.entities.RegistroCumplimientoEntity
import com.adrian.habitosplus.domain.model.Habito
import com.adrian.habitosplus.domain.model.RegistroCumplimiento
import com.adrian.habitosplus.domain.repository.HabitoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar

class HabitoRepositoryImpl(
    private val habitoDao: HabitoDao,
    private val registroDao: RegistroCumplimientoDao
) : HabitoRepository {

    override fun getAllHabitos(): Flow<List<Habito>> {
        return combine(
            habitoDao.getAllHabitos(),
            registroDao.getAllRegistros()
        ) { habitosEntities, todosLosRegistros ->
            habitosEntities.map { entity ->
                val registrosDeEsteHabito = todosLosRegistros.filter { it.idHabito == entity.id }
                entity.toDomain(racha = calcularRacha(registrosDeEsteHabito))
            }
        }
    }

    override suspend fun getHabitoById(id: Int): Habito? {
        val entity = habitoDao.getHabitoById(id) ?: return null
        val registros = registroDao.getRegistrosByHabito(id).first()
        return entity.toDomain(racha = calcularRacha(registros))
    }

    override suspend fun insertHabito(habito: Habito): Long {
        return habitoDao.insertHabito(habito.toEntity())
    }

    override suspend fun updateHabito(habito: Habito) {
        habitoDao.updateHabito(habito.toEntity())
    }

    override suspend fun deleteHabito(habito: Habito) {
        habitoDao.deleteHabito(habito.toEntity())
    }

    override fun getRegistrosByHabito(idHabito: Int): Flow<List<RegistroCumplimiento>> {
        return registroDao.getRegistrosByHabito(idHabito).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun marcarCumplidoHoy(idHabito: Int, fotoUri: String?) {
        val hoy = inicioDelDia(System.currentTimeMillis())

        registroDao.insertRegistro(
            RegistroCumplimientoEntity(
                idHabito = idHabito,
                fecha = hoy,
                fotoUri = fotoUri
            )
        )
    }

    override suspend fun deleteRegistro(registro: RegistroCumplimiento) {
        registroDao.deleteRegistro(registro.toEntity())
    }

    // --- Cálculo de racha ---

    private fun inicioDelDia(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun calcularRacha(registros: List<RegistroCumplimientoEntity>): Int {
        if (registros.isEmpty()) return 0

        val diasUnicos = registros.map { inicioDelDia(it.fecha) }.distinct().sortedDescending()

        val hoy = inicioDelDia(System.currentTimeMillis())
        val unDia = 24 * 60 * 60 * 1000L

        var racha = 0
        var diaEsperado = hoy

        for (dia in diasUnicos) {
            if (dia == diaEsperado) {
                racha++
                diaEsperado -= unDia
            } else if (dia == diaEsperado + unDia && racha == 0) {
                racha++
                diaEsperado = dia - unDia
            } else {
                break
            }
        }

        return racha
    }
}

// --- Funciones de mapeo entre Entity (Room) y Modelo de dominio ---

private fun HabitoEntity.toDomain(racha: Int = 0): Habito {
    return Habito(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        colorTag = colorTag,
        fechaCreacion = fechaCreacion,
        rachaActual = racha
    )
}

private fun Habito.toEntity(): HabitoEntity {
    return HabitoEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        colorTag = colorTag,
        fechaCreacion = fechaCreacion
    )
}

private fun RegistroCumplimientoEntity.toDomain(): RegistroCumplimiento {
    return RegistroCumplimiento(
        id = id,
        idHabito = idHabito,
        fecha = fecha,
        fotoUri = fotoUri
    )
}

private fun RegistroCumplimiento.toEntity(): RegistroCumplimientoEntity {
    return RegistroCumplimientoEntity(
        id = id,
        idHabito = idHabito,
        fecha = fecha,
        fotoUri = fotoUri
    )
}