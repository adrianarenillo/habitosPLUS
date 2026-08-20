package com.adrian.habitosplus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adrian.habitosplus.di.AppContainer
import com.adrian.habitosplus.di.ViewModelFactory
import com.adrian.habitosplus.ui.screens.agregarhabito.AgregarHabitoScreen
import com.adrian.habitosplus.ui.screens.agregarhabito.AgregarHabitoViewModel
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
                },
                onAgregarClick = {
                    navController.navigate(Screen.AgregarHabito.route)
                }
            )
        }

        composable(Screen.DetalleHabito.route) { backStackEntry ->
            val habitoId = backStackEntry.arguments?.getString("habitoId")?.toIntOrNull() ?: 0
            val viewModel: DetalleHabitoViewModel = viewModel(factory = factory)
            DetalleHabitoScreen(
                habitoId = habitoId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onEditarClick = {
                    navController.navigate(Screen.AgregarHabito.createRoute(habitoId))
                }
            )
        }

        composable(Screen.Ajustes.route) {
            val viewModel: AjustesViewModel = viewModel(factory = factory)
            AjustesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            Screen.AgregarHabito.route,
            arguments = listOf(
                navArgument("habitoId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val habitoId = backStackEntry.arguments?.getInt("habitoId") ?: -1
            val viewModel: AgregarHabitoViewModel = viewModel(factory = factory)
            AgregarHabitoScreen(
                viewModel = viewModel,
                habitoId = habitoId.takeIf { it != -1 },
                onBackClick = { navController.popBackStack() },
                onHabitoGuardado = { navController.popBackStack() }
            )
        }
    }
}