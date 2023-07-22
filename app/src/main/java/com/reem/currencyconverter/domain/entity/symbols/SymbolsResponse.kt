package com.reem.currencyconverter.domain.entity.symbols

import com.reem.currencyconverter.domain.entity.apiError.FixerApiError

data class SymbolsResponse(
    val success: Boolean,
    val symbols: Symbols?,
    val error: FixerApiError?
)