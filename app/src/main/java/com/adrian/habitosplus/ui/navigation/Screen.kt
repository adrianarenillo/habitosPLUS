package com.adrian.habitosplus.ui.navigation

sealed class Screen(val route: String) {
    object ListaHabitos : Screen("lista_habitos")
    object DetalleHabito : Screen("detalle_habito/{habitoId}") {
        fun createRoute(habitoId: Int) = "detalle_habito/$habitoId"
    }
    object Ajustes : Screen("ajustes")
}