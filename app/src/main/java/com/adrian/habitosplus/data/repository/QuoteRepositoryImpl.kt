package com.adrian.habitosplus.data.repository

import com.adrian.habitosplus.data.remote.ZenQuotesApi
import com.adrian.habitosplus.domain.model.Quote
import com.adrian.habitosplus.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val api: ZenQuotesApi
) : QuoteRepository {

    override suspend fun getRandomQuote(): Result<Quote> {
        return try {
            val respuesta = api.getRandomQuote()
            val dto = respuesta.firstOrNull()

            if (dto != null) {
                Result.success(Quote(texto = dto.q, autor = dto.a))
            } else {
                Result.failure(Exception("La API no devolvió ninguna frase"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}