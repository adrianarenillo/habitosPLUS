package com.adrian.habitosplus.ui.screens.agregarhabito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.habitosplus.domain.model.Habito
import com.adrian.habitosplus.domain.repository.HabitoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AgregarHabitoViewModel(
    private val habitoRepository: HabitoRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onNombreChange(value: String) {
        _nombre.value = value
        _error.value = null
    }

    fun onDescripcionChange(value: String) {
        _descripcion.value = value
    }

    fun guardarHabito(onGuardado: () -> Unit) {
        val nombreActual = _nombre.value.trim()

        if (nombreActual.isEmpty()) {
            _error.value = "El nombre del hábito no puede estar vacío"
            return
        }

        viewModelScope.launch {
            habitoRepository.insertHabito(
                Habito(
                    nombre = nombreActual,
                    descripcion = _descripcion.value.trim().ifEmpty { null },
                    colorTag = 0,
                    fechaCreacion = System.currentTimeMillis()
                )
            )
            onGuardado()
        }
    }
}