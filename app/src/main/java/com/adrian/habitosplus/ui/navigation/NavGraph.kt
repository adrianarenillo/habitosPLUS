package com.adrian.habitosplus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adrian.habitosplus.di.AppContainer
import com.adrian.habitosplus.di.ViewModelFactory
import com.adrian.habitosplus.ui.screens.ajustes.AjustesScreen
import com.adrian.habitosplus.ui.screens.ajustes.AjustesViewModel
import com.adrian.habitosplus.ui.screens.detallehabito.DetalleHabitoScreen
import com.adrian.habitosplus.ui.screens.detallehabito.DetalleHabitoViewModel
import com.adrian.habitosplus.ui.screens.listahabitos.ListaHabitosScreen
import com.adrian.habitosplus.ui.screens.listahabitos.ListaHabitosViewModel

@Composable
fun NavGraph(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val factory = ViewModelFactory(appContainer)

    NavHost(
        navController = navController,
        startDestination = Screen.ListaHabitos.route
    ) {
        composable(Screen.ListaHabitos.route) {
            val viewModel: ListaHabitosViewModel = viewModel(factory = factory)
            ListaHabitosScreen(
                viewModel = viewModel,
                onHabitoClick = { id ->
                    navController.navigate(Screen.DetalleHabito.createRoute(id))
                },
                onAjustesClick = {
                    navController.navigate(Screen.Ajustes.route)
                }
            )
        }

        composable(Screen.DetalleHabito.route) { backStackEntry ->
            val habitoId = backStackEntry.arguments?.getString("habitoId")?.toIntOrNull() ?: 0
            val viewModel: DetalleHabitoViewModel = viewModel(factory = factory)
            DetalleHabitoScreen(
                habitoId = habitoId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Ajustes.route) {
            val viewModel: AjustesViewModel = viewModel(factory = factory)
            AjustesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}