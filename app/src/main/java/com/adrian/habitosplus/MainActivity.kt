package com.adrian.habitosplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.adrian.habitosplus.di.AppContainer
import com.adrian.habitosplus.ui.navigation.NavGraph
import com.adrian.habitosplus.ui.theme.HabitosPLUSTheme

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer(applicationContext)

        enableEdgeToEdge()
        setContent {
            val modoOscuro by appContainer.settingsDataStore.modoOscuro.collectAsState(initial = false)

            HabitosPLUSTheme(darkTheme = modoOscuro) {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavGraph(appContainer = appContainer)
                }
            }
        }
    }
}