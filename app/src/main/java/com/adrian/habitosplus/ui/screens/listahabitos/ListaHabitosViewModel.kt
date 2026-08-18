package com.adrian.habitosplus.ui.screens.listahabitos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.habitosplus.domain.model.Habito
import com.adrian.habitosplus.domain.model.Quote
import com.adrian.habitosplus.domain.repository.HabitoRepository
import com.adrian.habitosplus.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class QuoteState {
    object Cargando : QuoteState()
    data class Exito(val quote: Quote) : QuoteState()
    data class Error(val mensaje: String) : QuoteState()
}

class ListaHabitosViewModel(
    private val habitoRepository: HabitoRepository,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private val _habitos = MutableStateFlow<List<Habito>>(emptyList())
    val habitos: StateFlow<List<Habito>> = _habitos.asStateFlow()

    private val _quoteState = MutableStateFlow<QuoteState>(QuoteState.Cargando)
    val quoteState: StateFlow<QuoteState> = _quoteState.asStateFlow()

    init {
        cargarHabitos()
        cargarFraseDelDia()
    }

    private fun cargarHabitos() {
        viewModelScope.launch {
            habitoRepository.getAllHabitos().collect { lista ->
                _habitos.value = lista
            }
        }
    }

    fun cargarFraseDelDia() {
        viewModelScope.launch {
            _quoteState.value = QuoteState.Cargando
            val resultado = quoteRepository.getRandomQuote()
            _quoteState.value = resultado.fold(
                onSuccess = { QuoteState.Exito(it) },
                onFailure = { QuoteState.Error(it.message ?: "Error al cargar la frase") }
            )
        }
    }

    fun marcarCumplidoHoy(idHabito: Int, fotoUri: String? = null) {
        viewModelScope.launch {
            habitoRepository.marcarCumplidoHoy(idHabito, fotoUri)
        }
    }
}