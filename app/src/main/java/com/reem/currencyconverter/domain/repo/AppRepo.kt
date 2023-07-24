package com.reem.currencyconverter.domain.repo

import com.reem.currencyconverter.domain.models.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.models.rates.RatesResponse
import com.reem.currencyconverter.domain.models.symbols.SymbolsResponse
import retrofit2.Response

interface AppRepo {
    suspend fun getSymbols(): Response<SymbolsResponse>

    suspend fun getRates(
        base: String,
        commaSeparatedSymbols: String
    ): Response<RatesResponse>

    suspend fun getHistoricalData(
        base: String,
        symbol: String,
        date: String
    ): Response<HistoricalDataResponse>
}