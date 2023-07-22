package com.reem.currencyconverter.domain.repo

import com.reem.currencyconverter.domain.entity.rates.RatesResponse
import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse
import retrofit2.Response

interface AppRepo {
    suspend fun getSymbols(): Response<SymbolsResponse>

    suspend fun getRates(
        base: String,
        commaSeparatedSymbols: String
    ): Response<RatesResponse>
}