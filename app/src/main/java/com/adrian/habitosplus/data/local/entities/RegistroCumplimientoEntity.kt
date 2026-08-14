package com.adrian.habitosplus.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "registros_cumplimiento",
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["id"],
            childColumns = ["idHabito"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RegistroCumplimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idHabito: Int,
    val fecha: Long,
    val fotoUri: String?
)