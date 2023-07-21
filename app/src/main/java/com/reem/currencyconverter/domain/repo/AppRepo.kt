package com.reem.currencyconverter.domain.repo

import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse

interface AppRepo {

    suspend fun getSymbols(): SymbolsResponse
}