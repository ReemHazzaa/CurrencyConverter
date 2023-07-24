package com.reem.currencyconverter.domain.models.historicalData

import com.reem.currencyconverter.domain.models.apiError.FixerApiError
import com.reem.currencyconverter.domain.models.rates.Rates

data class HistoricalDataResponse(
    val base: String?,
    val date: String?,
    val historical: Boolean?,
    val success: Boolean,
    val timestamp: Int?,
    val rates: Rates?,
    val error: FixerApiError?
)