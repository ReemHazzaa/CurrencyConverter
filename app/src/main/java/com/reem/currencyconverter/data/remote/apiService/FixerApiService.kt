package com.reem.currencyconverter.data.remote.apiService

import com.reem.currencyconverter.domain.entity.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.entity.rates.RatesResponse
import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FixerApiService {

    @GET("symbols")
    suspend fun getSymbols(): Response<SymbolsResponse>

    @GET("latest")
    suspend fun getRates(
        @Query("base") base: String,
        @Query("symbols") commaSeparatedSymbols: String
    ): Response<RatesResponse>

    @GET("{Date}")
    suspend fun getHistoricalData(
        @Path("Date") date: String, // Required Date Format YYYY-MM-DD
        @Query("base") base: String,
        @Query("symbols") symbol: String
    ): Response<HistoricalDataResponse>
}