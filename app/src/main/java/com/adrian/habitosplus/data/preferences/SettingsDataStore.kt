package com.adrian.habitosplus.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val MODO_OSCURO_KEY = booleanPreferencesKey("modo_oscuro")
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[MODO_OSCURO_KEY] ?: false
    }

    suspend fun setModoOscuro(activado: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MODO_OSCURO_KEY] = activado
        }
    }
}