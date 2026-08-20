package com.adrian.habitosplus.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitos")
data class HabitoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String?,
    val colorTag: Int,
    val fechaCreacion: Long,
    val imagenFondoUri: String? = null
)