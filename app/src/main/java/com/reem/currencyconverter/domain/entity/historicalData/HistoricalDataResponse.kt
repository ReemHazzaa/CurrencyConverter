package com.reem.currencyconverter.domain.entity.historicalData

import com.reem.currencyconverter.domain.entity.apiError.FixerApiError
import com.reem.currencyconverter.domain.entity.rates.Rates

data class HistoricalDataResponse(
    val base: String?,
    val date: String?,
    val historical: Boolean?,
    val success: Boolean,
    val timestamp: Int?,
    val rates: Rates?,
    val error: FixerApiError?
)