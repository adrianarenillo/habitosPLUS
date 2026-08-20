package com.adrian.habitosplus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adrian.habitosplus.data.local.entities.HabitoEntity
import com.adrian.habitosplus.data.local.entities.RegistroCumplimientoEntity

@Database(
    entities = [HabitoEntity::class, RegistroCumplimientoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitoDao(): HabitoDao
    abstract fun registroCumplimientoDao(): RegistroCumplimientoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitosplus_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}