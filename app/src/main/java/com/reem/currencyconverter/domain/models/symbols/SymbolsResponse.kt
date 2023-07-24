package com.reem.currencyconverter.domain.models.symbols

import com.reem.currencyconverter.domain.models.apiError.FixerApiError

data class SymbolsResponse(
    val success: Boolean,
    val symbols: Symbols?,
    val error: FixerApiError?
)