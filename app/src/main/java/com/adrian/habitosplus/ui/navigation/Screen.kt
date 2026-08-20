package com.adrian.habitosplus.ui.navigation

sealed class Screen(val route: String) {
    object ListaHabitos : Screen("lista_habitos")
    object DetalleHabito : Screen("detalle_habito/{habitoId}") {
        fun createRoute(habitoId: Int) = "detalle_habito/$habitoId"
    }
    object Ajustes : Screen("ajustes")
    object AgregarHabito : Screen("agregar_habito?habitoId={habitoId}") {
        fun createRoute(habitoId: Int? = null) =
            if (habitoId != null) "agregar_habito?habitoId=$habitoId" else "agregar_habito"
    }
}