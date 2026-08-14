package com.adrian.habitosplus.domain.repository

import com.adrian.habitosplus.domain.model.Quote

interface QuoteRepository {
    suspend fun getRandomQuote(): Result<Quote>
}