package com.reem.currencyconverter.app.entity

import com.reem.currencyconverter.domain.entity.rates.Rates

data class HistoricalDataUI(
    val listOfHistoricalData: List<HistoricalDayUI>

)

data class HistoricalDayUI(
    val date: String,
    val base: String,
    val rates: Rates
)
