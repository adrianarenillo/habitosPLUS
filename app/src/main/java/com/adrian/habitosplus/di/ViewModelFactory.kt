package com.adrian.habitosplus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adrian.habitosplus.ui.screens.ajustes.AjustesViewModel
import com.adrian.habitosplus.ui.screens.detallehabito.DetalleHabitoViewModel
import com.adrian.habitosplus.ui.screens.listahabitos.ListaHabitosViewModel

class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListaHabitosViewModel::class.java) -> {
                ListaHabitosViewModel(
                    appContainer.habitoRepository,
                    appContainer.quoteRepository
                ) as T
            }
            modelClass.isAssignableFrom(DetalleHabitoViewModel::class.java) -> {
                DetalleHabitoViewModel(
                    appContainer.habitoRepository
                ) as T
            }
            modelClass.isAssignableFrom(AjustesViewModel::class.java) -> {
                AjustesViewModel(
                    appContainer.settingsDataStore
                ) as T
            }
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}