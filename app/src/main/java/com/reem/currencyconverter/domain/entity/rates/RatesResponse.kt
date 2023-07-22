package com.reem.currencyconverter.domain.entity.rates

import com.reem.currencyconverter.domain.entity.apiError.FixerApiError

data class RatesResponse(
    val base: String?,
    val date: String?,
    val rates: Rates?,
    val success: Boolean,
    val timestamp: Int?,
    val error: FixerApiError?
)