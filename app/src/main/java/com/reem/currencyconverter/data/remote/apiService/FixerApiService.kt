package com.reem.currencyconverter.data.remote.apiService

import com.reem.currencyconverter.domain.entity.rates.RatesResponse
import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApiService {

    @GET("symbols")
    suspend fun getSymbols(): Response<SymbolsResponse>

    @GET("latest")
    suspend fun getRates(
        @Query("base") base: String,
        @Query("symbols") commaSeparatedSymbols: String
    ): Response<RatesResponse>
}