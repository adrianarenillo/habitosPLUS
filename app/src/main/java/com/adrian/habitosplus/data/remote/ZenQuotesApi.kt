package com.adrian.habitosplus.data.remote

import com.adrian.habitosplus.data.remote.dto.QuoteDto
import retrofit2.http.GET

interface ZenQuotesApi {
    @GET("api/phrase")
    suspend fun getRandomQuote(): QuoteDto
}