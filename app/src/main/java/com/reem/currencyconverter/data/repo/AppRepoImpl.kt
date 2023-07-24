package com.reem.currencyconverter.data.repo

import com.reem.currencyconverter.data.remote.apiService.FixerApiService
import com.reem.currencyconverter.domain.models.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.models.rates.RatesResponse
import com.reem.currencyconverter.domain.models.symbols.SymbolsResponse
import com.reem.currencyconverter.domain.repo.AppRepo
import retrofit2.Response
import javax.inject.Inject

class AppRepoImpl @Inject constructor(
    private val fixerApiService: FixerApiService
) : AppRepo {

    override suspend fun getSymbols(): Response<SymbolsResponse> {
        return fixerApiService.getSymbols()
    }

    override suspend fun getRates(
        base: String,
        commaSeparatedSymbols: String
    ): Response<RatesResponse> {
        return fixerApiService.getRates(base, commaSeparatedSymbols)
    }

    override suspend fun getHistoricalData(
        base: String,
        symbol: String,
        date: String
    ): Response<HistoricalDataResponse> {
        return fixerApiService.getHistoricalData(date, base, symbol)
    }
}