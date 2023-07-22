package com.reem.currencyconverter.data.repo

import com.reem.currencyconverter.data.remote.apiService.FixerApiService
import com.reem.currencyconverter.domain.entity.rates.RatesResponse
import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse
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
}