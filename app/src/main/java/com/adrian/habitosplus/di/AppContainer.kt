package com.adrian.habitosplus.di

import android.content.Context
import com.adrian.habitosplus.data.local.AppDatabase
import com.adrian.habitosplus.data.preferences.SettingsDataStore
import com.adrian.habitosplus.data.remote.RetrofitInstance
import com.adrian.habitosplus.data.repository.HabitoRepositoryImpl
import com.adrian.habitosplus.data.repository.QuoteRepositoryImpl
import com.adrian.habitosplus.domain.repository.HabitoRepository
import com.adrian.habitosplus.domain.repository.QuoteRepository

class AppContainer(context: Context) {

    private val database = AppDatabase.getDatabase(context)

    val habitoRepository: HabitoRepository = HabitoRepositoryImpl(
        habitoDao = database.habitoDao(),
        registroDao = database.registroCumplimientoDao()
    )

    val quoteRepository: QuoteRepository = QuoteRepositoryImpl(
        api = RetrofitInstance.api
    )

    val settingsDataStore = SettingsDataStore(context)
}