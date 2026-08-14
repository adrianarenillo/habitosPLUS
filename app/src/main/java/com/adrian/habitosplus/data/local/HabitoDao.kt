package com.adrian.habitosplus.data.local

import androidx.room.*
import com.adrian.habitosplus.data.local.entities.HabitoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {

    @Query("SELECT * FROM habitos ORDER BY fechaCreacion DESC")
    fun getAllHabitos(): Flow<List<HabitoEntity>>

    @Query("SELECT * FROM habitos WHERE id = :id")
    suspend fun getHabitoById(id: Int): HabitoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabito(habito: HabitoEntity): Long

    @Update
    suspend fun updateHabito(habito: HabitoEntity)

    @Delete
    suspend fun deleteHabito(habito: HabitoEntity)
}