package com.adrian.habitosplus.ui.screens.ajustes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.habitosplus.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val modoOscuro: StateFlow<Boolean> = settingsDataStore.modoOscuro
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun setModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setModoOscuro(activado)
        }
    }
}