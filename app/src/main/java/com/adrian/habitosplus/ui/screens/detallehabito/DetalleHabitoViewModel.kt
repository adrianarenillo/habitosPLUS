package com.adrian.habitosplus.ui.screens.detallehabito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.habitosplus.domain.model.Habito
import com.adrian.habitosplus.domain.model.RegistroCumplimiento
import com.adrian.habitosplus.domain.repository.HabitoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleHabitoViewModel(
    private val habitoRepository: HabitoRepository
) : ViewModel() {

    private val _habito = MutableStateFlow<Habito?>(null)
    val habito: StateFlow<Habito?> = _habito.asStateFlow()

    private val _registros = MutableStateFlow<List<RegistroCumplimiento>>(emptyList())
    val registros: StateFlow<List<RegistroCumplimiento>> = _registros.asStateFlow()

    fun cargarHabito(habitoId: Int) {
        viewModelScope.launch {
            _habito.value = habitoRepository.getHabitoById(habitoId)
            habitoRepository.getRegistrosByHabito(habitoId).collect { lista ->
                _registros.value = lista
            }
        }
    }

    fun eliminarHabito(onEliminado: () -> Unit) {
        viewModelScope.launch {
            _habito.value?.let {
                habitoRepository.deleteHabito(it)
                onEliminado()
            }
        }
    }
}