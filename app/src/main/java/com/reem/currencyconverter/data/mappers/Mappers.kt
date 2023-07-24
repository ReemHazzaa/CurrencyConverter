package com.reem.currencyconverter.data.mappers

import com.google.gson.Gson
import com.reem.currencyconverter.app.models.historicalData.HistoricalDataUI
import com.reem.currencyconverter.app.models.historicalData.HistoricalDayUI
import com.reem.currencyconverter.domain.models.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.models.rates.Rates
import com.reem.currencyconverter.domain.models.symbols.Symbols
import retrofit2.Response

fun mapSymbolsObjectToStringList(symbols: Symbols): MutableList<String> {
    val list = mutableListOf<String>()
    for (field in symbols.javaClass.declaredFields) {
        list.add(field.name)
    }
    return list
}

fun convertCurrency(fromAmount: String?, rates: Rates): String {
    var targetRate = getConversionRateValue(rates)
    if (targetRate.isBlank()) targetRate = "AED"
    return (fromAmount?.toDouble()
        ?.times(targetRate.toDouble()) ?: 0.0).toString()

}

fun getConversionRateValue(rates: Rates): String {
    val gson = Gson()
    var ratesJson = gson.toJson(rates, Rates::class.java)
    ratesJson = ratesJson.toString().substringAfter(":")
    ratesJson = ratesJson.substring(1, ratesJson.length - 2)
    return ratesJson
}

fun mapHistoricalApiResponsesToUI(
    firstDayValue: Response<HistoricalDataResponse>,
    secondDayValue: Response<HistoricalDataResponse>,
    thirdDayValue: Response<HistoricalDataResponse>
): HistoricalDataUI {
    val first = firstDayValue.body()
    val second = secondDayValue.body()
    val third = thirdDayValue.body()
    val firstDay =
        first?.rates?.let { HistoricalDayUI(first.date ?: "N/A", first.base ?: "N/A", it) }
    val secondDay =
        second?.rates?.let { HistoricalDayUI(second.date ?: "N/A", second.base ?: "N/A", it) }
    val thirdDay =
        third?.rates?.let { HistoricalDayUI(third.date ?: "N/A", third.base ?: "N/A", it) }

    return HistoricalDataUI(listOf(firstDay!!, secondDay!!, thirdDay!!))
}