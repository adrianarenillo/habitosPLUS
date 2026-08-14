package com.adrian.habitosplus.data.local

import androidx.room.*
import com.adrian.habitosplus.data.local.entities.RegistroCumplimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroCumplimientoDao {

    @Query("SELECT * FROM registros_cumplimiento WHERE idHabito = :idHabito ORDER BY fecha DESC")
    fun getRegistrosByHabito(idHabito: Int): Flow<List<RegistroCumplimientoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistro(registro: RegistroCumplimientoEntity)

    @Delete
    suspend fun deleteRegistro(registro: RegistroCumplimientoEntity)
}