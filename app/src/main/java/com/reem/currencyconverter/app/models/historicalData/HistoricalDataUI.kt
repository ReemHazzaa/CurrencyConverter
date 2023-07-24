package com.reem.currencyconverter.app.models.historicalData

import com.reem.currencyconverter.domain.models.rates.Rates

data class HistoricalDataUI(
    val listOfHistoricalData: List<HistoricalDayUI>,
    val otherCurrencies: List<RateUiItem>
)

data class HistoricalDayUI(
    val date: String,
    val base: String,
    val rates: Rates
)

data class RateUiItem(
    val name: String,
    val value: String
)
