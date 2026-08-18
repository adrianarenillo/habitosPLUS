package com.adrian.habitosplus.data.repository

import com.adrian.habitosplus.data.remote.ZenQuotesApi
import com.adrian.habitosplus.domain.model.Quote
import com.adrian.habitosplus.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val api: ZenQuotesApi
) : QuoteRepository {

    override suspend fun getRandomQuote(): Result<Quote> {
        return try {
            val dto = api.getRandomQuote()
            Result.success(Quote(texto = dto.phrase, autor = dto.author))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}