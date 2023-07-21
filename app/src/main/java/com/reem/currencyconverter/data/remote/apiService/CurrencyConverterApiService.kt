package com.reem.currencyconverter.data.remote.apiService

import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse
import retrofit2.http.GET

interface CurrencyConverterApiService {
    @GET("symbols")
    suspend fun getSymbols(): SymbolsResponse
}